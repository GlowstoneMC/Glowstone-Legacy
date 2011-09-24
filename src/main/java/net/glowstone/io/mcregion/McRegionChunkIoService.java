package net.glowstone.io.mcregion;

import java.io.DataOutputStream;
import java.io.File;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.tools.javac.code.Attribute;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowEntity;
import net.glowstone.io.ChunkIoService;
import net.glowstone.io.mcregion.region.RegionFile;
import net.glowstone.io.mcregion.region.RegionFileCache;
import net.glowstone.GlowChunk;
import net.glowstone.GlowWorld;
import net.glowstone.io.nbt.blockstate.NbtBlockStateStorage;
import net.glowstone.io.nbt.blockstate.NbtBlockStateStorageLookup;
import net.glowstone.io.nbt.entity.NbtEntityStorage;
import net.glowstone.io.nbt.entity.NbtEntityStorageLookup;
import net.glowstone.util.nbt.ByteArrayTag;
import net.glowstone.util.nbt.ByteTag;
import net.glowstone.util.nbt.CompoundTag;
import net.glowstone.util.nbt.IntTag;
import net.glowstone.util.nbt.ListTag;
import net.glowstone.util.nbt.NBTInputStream;
import net.glowstone.util.nbt.NBTOutputStream;
import net.glowstone.util.nbt.StringTag;
import net.glowstone.util.nbt.Tag;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

/**
 * An implementation of the {@link net.glowstone.io.ChunkIoService} which reads and writes
 * McRegion maps.
 * <p />
 * Information on the McRegion file format can be found on the
 * <a href="http://mojang.com/2011/02/16/minecraft-save-file-format-in-beta-1-3">Mojang</a>
 * blog.
 * @author Graham Edgecombe
 */
public final class McRegionChunkIoService implements ChunkIoService {

    /**
     * The size of a region - a 32x32 group of chunks.
     */
    private static final int REGION_SIZE = 32;

    /**
     * The root directory of the map.
     */
    private File dir;

    /**
     * The region file cache.
     */
    private RegionFileCache cache = new RegionFileCache();

    // TODO: consider the session.lock file

    public McRegionChunkIoService() {
        this(new File("world"));
    }

    public McRegionChunkIoService(File dir) {
        this.dir = dir;
    }

    public GlowChunk read(GlowWorld world, int x, int z) throws IOException {
        RegionFile region = cache.getRegionFile(dir, x, z);
        int regionX = x & (REGION_SIZE - 1);
        int regionZ = z & (REGION_SIZE - 1);
        if (!region.hasChunk(regionX, regionZ)){
            return null;
        }

        DataInputStream in = region.getChunkDataInputStream(regionX, regionZ);
        GlowChunk chunk = new GlowChunk(world, x, z);

        NBTInputStream nbt = new NBTInputStream(in, false);
        CompoundTag tag = (CompoundTag) nbt.readTag();
        Map<String, Tag> levelTags = ((CompoundTag) tag.getValue().get("Level")).getValue();

        byte[] tileData = ((ByteArrayTag) levelTags.get("Blocks")).getValue();
        chunk.setTypes(tileData);
        chunk.setPopulated(((ByteTag) levelTags.get("TerrainPopulated")).getValue() == 1);

        byte[] skyLightData = ((ByteArrayTag) levelTags.get("SkyLight")).getValue();
        byte[] blockLightData = ((ByteArrayTag) levelTags.get("BlockLight")).getValue();
        byte[] metaData = ((ByteArrayTag) levelTags.get("Data")).getValue();

        for (int cx = 0; cx < GlowChunk.WIDTH; cx++) {
            for (int cz = 0; cz < GlowChunk.HEIGHT; cz++) {
                for (int cy = 0; cy < GlowChunk.DEPTH; cy++) {
                    boolean mostSignificantNibble = ((cx * GlowChunk.HEIGHT + cz) * GlowChunk.DEPTH + cy) % 2 == 1;
                    int offset = ((cx * GlowChunk.HEIGHT + cz) * GlowChunk.DEPTH + cy) / 2;

                    int skyLight, blockLight, meta;
                    if (mostSignificantNibble) {
                        skyLight = (skyLightData[offset] & 0xF0) >> 4;
                        blockLight = (blockLightData[offset] & 0xF0) >> 4;
                        meta = (metaData[offset] & 0xF0) >> 4;
                    } else {
                        skyLight = skyLightData[offset] & 0x0F;
                        blockLight = blockLightData[offset] & 0x0F;
                        meta = metaData[offset] & 0x0F;
                    }

                    chunk.setSkyLight(cx, cz, cy, skyLight);
                    chunk.setBlockLight(cx, cz, cy, blockLight);
                    chunk.setMetaData(cx, cz, cy, meta);
                }
            }
        }
        List<CompoundTag> storedTileEntities = ((ListTag<CompoundTag>)levelTags.get("TileEntities")).getValue();
        for (CompoundTag tileEntityTag : storedTileEntities) {
            ((IntTag)tileEntityTag.getValue().get("x")).getValue();
            GlowBlockState state = world.getBlockAt(((IntTag)tileEntityTag.getValue().get("x")).getValue(),
                    ((IntTag)tileEntityTag.getValue().get("y")).getValue(), ((IntTag)tileEntityTag.getValue().get("z")).getValue()).getState();
            if (state.getClass() != GlowBlockState.class) {
                state.load(tileEntityTag);
            }
        }

        return chunk;
    }

    public void write(int x, int z, GlowChunk chunk) throws IOException {
        RegionFile region = cache.getRegionFile(dir, x, z);
        int regionX = x & (REGION_SIZE - 1);
        int regionZ = z & (REGION_SIZE - 1);

        DataOutputStream out = region.getChunkDataOutputStream(regionX, regionZ);
        NBTOutputStream nbt = new NBTOutputStream(out, false);
        Map<String, Tag> levelTags = new HashMap<String, Tag>();

        byte[] skyLightData = new byte[GlowChunk.DEPTH * GlowChunk.WIDTH * GlowChunk.HEIGHT];
        byte[] blockLightData = new byte[GlowChunk.DEPTH * GlowChunk.WIDTH * GlowChunk.HEIGHT];
        byte[] metaData = new byte[GlowChunk.DEPTH * GlowChunk.WIDTH * GlowChunk.HEIGHT];
        byte[] heightMap = new byte[GlowChunk.WIDTH * GlowChunk.HEIGHT];

        for (int cx = 0; cx < GlowChunk.WIDTH; cx++) {
            for (int cz = 0; cz < GlowChunk.HEIGHT; cz++) {
                int heightOffset = (cx * GlowChunk.HEIGHT + cz) / 2;
                heightMap[heightOffset] = (byte)chunk.getWorld().getHighestBlockYAt(x > 0 ? x * cx : cx, z > 0 ? z * cz : cz);
                for (int cy = 0; cy < GlowChunk.DEPTH; cy++) {
                    int offset = ((cx * GlowChunk.HEIGHT + cz) * GlowChunk.DEPTH + cy) / 2;
                    skyLightData[offset] = (byte) ((chunk.getSkyLight(cx, cz, cy) << 4) | chunk.getSkyLight(cx, cz, cy));
					blockLightData[offset] = (byte) ((chunk.getBlockLight(cx, cz, cy) << 4) | chunk.getBlockLight(cx, cz, cy));
					metaData[offset] = (byte) ((chunk.getMetaData(cx, cz, cy) << 4) | chunk.getMetaData(cx, cz, cy));
                }
            }
        }

        levelTags.put("Blocks", new ByteArrayTag("Blocks", chunk.getTypes()));
        levelTags.put("SkyLight", new ByteArrayTag("SkyLight", skyLightData));
        levelTags.put("BlockLight", new ByteArrayTag("BlockLight", blockLightData));
        levelTags.put("Data", new ByteArrayTag("Data", metaData));
        levelTags.put("HeightMap", new ByteArrayTag("HeightMap", heightMap));

        levelTags.put("xPos", new IntTag("xPos", regionX));
        levelTags.put("zPos", new IntTag("zPos", regionZ));
        levelTags.put("TerrainPopulated", new ByteTag("TerrainPopulated", (byte)(chunk.getPopulated() ? 1 : 0)));

        List<CompoundTag> entities = new ArrayList<CompoundTag>();
        /* for (Entity entity : chunk.getEntities()) {
            GlowEntity glowEntity = (GlowEntity) entity;
            NbtEntityStorage store = NbtEntityStorageLookup.find(glowEntity.getClass());
            if (store == null)
                continue;
            entities.add(store.save(glowEntity));
        } */
        levelTags.put("Entities", new ListTag<CompoundTag>("Entities", CompoundTag.class, entities)); // TODO: entity storage
        List<CompoundTag> tileEntities = new ArrayList<CompoundTag>();
        for (BlockState state : chunk.getTileEntities()) {
            GlowBlockState glowState = (GlowBlockState) state;
            if (glowState.getClass() != GlowBlockState.class) {
                tileEntities.add(glowState.save());
            }
        }
        levelTags.put("TileEntities", new ListTag<CompoundTag>("TileEntities", CompoundTag.class, tileEntities));
        Map<String, Tag> levelOut = new HashMap<String, Tag>();
        levelOut.put("Level", new CompoundTag("Level", levelTags));
        nbt.writeTag(new CompoundTag("", levelOut));
        nbt.close();

    }

}
