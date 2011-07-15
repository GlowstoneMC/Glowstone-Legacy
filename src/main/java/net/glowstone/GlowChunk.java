package net.glowstone;

import net.glowstone.block.GlowBlockState;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

import net.glowstone.block.GlowBlock;
import net.glowstone.msg.CompressedChunkMessage;
import net.glowstone.msg.Message;

import java.util.*;

/**
 * Represents a chunk of the map.
 * @author Graham Edgecombe
 */
public final class GlowChunk implements Chunk {

    /**
     * The radius (not including the current chunk) of the chunks that the
     * player can see.
     */
    public static final int VISIBLE_RADIUS = 8;

    /**
     * A chunk key represents the X and Z coordinates of a chunk and implements
     * the {@link #hashCode()} and {@link #equals(Object)} methods making it
     * suitable for use as a key in a hash table or set.
     * @author Graham Edgecombe
     */
    public static final class Key {

        /**
         * The coordinates.
         */
        private final int x, z;

        /**
         * Creates a new chunk key with the specified X and Z coordinates.
         * @param x The X coordinate.
         * @param z The Z coordinate.
         */
        public Key(int x, int z) {
            this.x = x;
            this.z = z;
        }

        /**
         * Gets the X coordinate.
         * @return The X coordinate.
         */
        public int getX() {
            return x;
        }

        /**
         * Gets the Z coordinate.
         * @return The Z coordinate.
         */
        public int getZ() {
            return z;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + z;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key other = (Key) obj;
            if (x != other.x)
                return false;
            if (z != other.z)
                return false;
            return true;
        }

    }

    /**
     * The dimensions of a chunk.
     */
    public static final int WIDTH = 16, HEIGHT = 16, DEPTH = 128;
    
    /**
     * The world of this chunk.
     */
    private final GlowWorld world;

    /**
     * The coordinates of this chunk.
     */
    private final int x, z;

    /**
     * The data in this chunk representing all of the blocks and their state.
     */
    private final byte[] types, metaData, skyLight, blockLight;
    
    /**
     * Whether the chunk has been populated by special features.
     * Used in map generation.
     */
    private boolean populated = false;

    /**
     * A hashmap of BlockStates
     */

    private Set<GlowBlockState> blockStates = Collections.synchronizedSet(new HashSet<GlowBlockState>());
    /**
     * Creates a new chunk with a specified X and Z coordinate.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     */
    public GlowChunk(GlowWorld world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
        this.types = new byte[WIDTH * HEIGHT * DEPTH];
        this.metaData = new byte[WIDTH * HEIGHT * DEPTH];
        this.skyLight = new byte[WIDTH * HEIGHT * DEPTH];
        this.blockLight = new byte[WIDTH * HEIGHT * DEPTH];
        
        for (int i = 0; i < WIDTH * HEIGHT * DEPTH; ++i) {
            skyLight[i] = 15;
        }
    }
    
    // ======== Basic stuff ========

    /**
     * Gets the world containing this chunk
     *
     * @return Parent World
     */
    public GlowWorld getWorld() {
        return world;
    }

    /**
     * Gets the X coordinate of this chunk.
     * @return The X coordinate of this chunk.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Z coordinate of this chunk.
     * @return The Z coordinate of this chunk.
     */
    public int getZ() {
        return z;
    }
    
    /**
     * Gets a block from this chunk
     *
     * @param x 0-15
     * @param y 0-127
     * @param z 0-15
     * @return the Block
     */
    public GlowBlock getBlock(int x, int y, int z) {
        return getWorld().getBlockAt(this.x << 4 | x, y, this.z << 4 | z);
    }

    public Entity[] getEntities() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BlockState[] getTileEntities() {
        return blockStates.toArray(new BlockState[blockStates.size()]);
    }

    public void addBlockState(GlowBlockState state) {
        blockStates.add(state);
    }

    public Set<GlowBlockState> getBlockStates() {
        return Collections.unmodifiableSet(blockStates);
    }
    
    /**
     * Capture thread-safe read-only snapshot of chunk data
     * @return ChunkSnapshot
     */
    public ChunkSnapshot getChunkSnapshot() {
        return getChunkSnapshot(true, false, false);
    }

    /**
     * Capture thread-safe read-only snapshot of chunk data
     * @param includeMaxblocky - if true, snapshot includes per-coordinate maximum Y values
     * @param includeBiome - if true, snapshot includes per-coordinate biome type
     * @param includeBiomeTempRain - if true, snapshot includes per-coordinate raw biome temperature and rainfall
     * @return ChunkSnapshot
     */
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxblocky, boolean includeBiome, boolean includeBiomeTempRain) {
        return new GlowChunkSnapshot(x, z, world, types, metaData, skyLight, blockLight, includeMaxblocky, includeBiome, includeBiomeTempRain);
    }
    
    /**
     * Gets whether this chunk has been populated by special features.
     * @return Population status.
     */
    public boolean getPopulated() {
        return populated;
    }
    
    /**
     * Sets the population status of this chunk.
     * @param populated Population status.
     */
    public void setPopulated(boolean populated) {
        this.populated = populated;
    }
    
    // ======== Data access ========

    /**
     * Gets the type of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The type.
     */
    public int getType(int x, int z, int y) {
        return types[coordToIndex(x, z, y)];
    }

    /**
     * Sets the types of all tiles within the chunk.
     * @param types The array of types.
     */
    public void setTypes(byte[] types){
        if (types.length != WIDTH * HEIGHT * DEPTH) {
            throw new IllegalArgumentException();
        }
        System.arraycopy(types, 0, this.types, 0, types.length);
    }

    public byte[] getTypes() {
        byte[] types = new byte[this.types.length];
        System.arraycopy(this.types, 0, types, 0, this.types.length);
        return types;
    }

    /**
     * Sets the type of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param type The type.
     */
    public void setType(int x, int z, int y, int type) {
        if (type < 0 || type >= 256)
            throw new IllegalArgumentException();

        types[coordToIndex(x, z, y)] = (byte) type;
    }

    /**
     * Gets the metadata of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The metadata.
     */
    public int getMetaData(int x, int z, int y) {
        return metaData[coordToIndex(x, z, y)];
    }

    /**
     * Sets the metadata of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param metaData The metadata.
     */
    public void setMetaData(int x, int z, int y, int metaData) {
        if (metaData < 0 || metaData >= 16)
            throw new IllegalArgumentException();

        this.metaData[coordToIndex(x, z, y)] = (byte) metaData;
    }

    /**
     * Gets the sky light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The sky light level.
     */
    public int getSkyLight(int x, int z, int y) {
        return skyLight[coordToIndex(x, z, y)];
    }

    /**
     * Sets the sky light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param skyLight The sky light level.
     */
    public void setSkyLight(int x, int z, int y, int skyLight) {
        if (skyLight < 0 || skyLight >= 16)
            throw new IllegalArgumentException();

        this.skyLight[coordToIndex(x, z, y)] = (byte) skyLight;
    }

    /**
     * Gets the block light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The block light level.
     */
    public int getBlockLight(int x, int z, int y) {
        return blockLight[coordToIndex(x, z, y)];
    }

    /**
     * Sets the block light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param blockLight The block light level.
     */
    public void setBlockLight(int x, int z, int y, int blockLight) {
        if (blockLight < 0 || blockLight >= 16)
            throw new IllegalArgumentException();

        this.blockLight[coordToIndex(x, z, y)] = (byte) blockLight;
    }
    
    // ======== Helper functions ========

    /**
     * Creates a new {@link Message} which can be sent to a client to stream
     * this chunk to them.
     * @return The {@link CompressedChunkMessage}.
     */
    public Message toMessage() {
        return new CompressedChunkMessage(x * GlowChunk.WIDTH, z * GlowChunk.HEIGHT, 0, WIDTH, HEIGHT, DEPTH, serializeTileData());
    }

    /**
     * Converts a three-dimensional coordinate to an index within the
     * one-dimensional arrays.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The index within the arrays.
     */
    private int coordToIndex(int x, int z, int y) {
        if (x < 0 || z < 0 || y < 0 || x >= WIDTH || z >= HEIGHT || y >= DEPTH)
            throw new IndexOutOfBoundsException();

        return (x * HEIGHT + z) * DEPTH + y;
    }

    /**
     * Serializes tile data into a byte array.
     * @return The byte array populated with the tile data.
     */
    private byte[] serializeTileData() {
        byte[] dest = new byte[((WIDTH * HEIGHT * DEPTH * 5) / 2)];

        System.arraycopy(types, 0, dest, 0, types.length);

        int pos = types.length;

        for (int i = 0; i < metaData.length; i += 2) {
            byte meta1 = metaData[i];
            byte meta2 = metaData[i + 1];
            dest[pos++] = (byte) ((meta2 << 4) | meta1);
        }

        for (int i = 0; i < skyLight.length; i += 2) {
            byte light1 = skyLight[i];
            byte light2 = skyLight[i + 1];
            dest[pos++] = (byte) ((light2 << 4) | light1);
        }

        for (int i = 0; i < blockLight.length; i += 2) {
            byte light1 = blockLight[i];
            byte light2 = blockLight[i + 1];
            dest[pos++] = (byte) ((light2 << 4) | light1);
        }

        return dest;
    }

}
