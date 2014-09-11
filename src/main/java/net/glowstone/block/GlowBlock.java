package net.glowstone.block;

import net.glowstone.GlowChunk;
import net.glowstone.GlowWorld;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.message.play.game.BlockChangeMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single block in a world.
 */
public final class GlowBlock implements Block {

    /**
     * The metadata store class for blocks.
     */
    private static final class BlockMetadataStore extends MetadataStoreBase<Block> implements MetadataStore<Block> {
        protected String disambiguate(Block subject, String metadataKey) {
            return subject.getWorld() + "," + subject.getX() + "," + subject.getY() + "," + subject.getZ() + ":" + metadataKey;
        }
    }

    /**
     * The metadata store for blocks.
     */
    private static final MetadataStore<Block> metadata = new BlockMetadataStore();

    private final GlowChunk chunk;
    private final int x;
    private final int y;
    private final int z;

    public GlowBlock(GlowChunk chunk, int x, int y, int z) {
        this.chunk = chunk;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Basics

    public GlowWorld getWorld() {
        return chunk.getWorld();
    }

    public GlowChunk getChunk() {
        return chunk;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Location getLocation() {
        return new Location(getWorld(), x, y, z);
    }

    public Location getLocation(Location loc) {
        if (loc == null) return null;
        loc.setWorld(getWorld());
        loc.setX(x);
        loc.setY(y);
        loc.setZ(z);
        return loc;
    }

    public TileEntity getTileEntity() {
        return chunk.getEntity(x & 0xf, y, z & 0xf);
    }

    public GlowBlockState getState() {
        TileEntity entity = getTileEntity();
        if (entity != null) {
            GlowBlockState state = entity.getState();
            if (state != null) {
                return state;
            }
        }
        return new GlowBlockState(this);
    }

    public Biome getBiome() {
        return getWorld().getBiome(x, z);
    }

    public void setBiome(Biome bio) {
        getWorld().setBiome(x, z, bio);
    }

    public double getTemperature() {
        return getWorld().getTemperature(x, z);
    }

    public double getHumidity() {
        return getWorld().getHumidity(x, z);
    }

    ////////////////////////////////////////////////////////////////////////////
    // getFace & getRelative

    public BlockFace getFace(Block block) {
        for (BlockFace face : BlockFace.values()) {
            if ((x + face.getModX() == block.getX()) &&
                    (y + face.getModY() == block.getY()) &&
                    (z + face.getModZ() == block.getZ())) {
                return face;
            }
        }
        return null;
    }

    public GlowBlock getRelative(int modX, int modY, int modZ) {
        return getWorld().getBlockAt(x + modX, y + modY, z + modZ);
    }

    public GlowBlock getRelative(BlockFace face) {
        return getRelative(face.getModX(), face.getModY(), face.getModZ());
    }

    public GlowBlock getRelative(BlockFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Type and typeid getters/setters

    public Material getType() {
        return Material.getMaterial(getTypeId());
    }

    public int getTypeId() {
        return chunk.getType(x & 0xf, z & 0xf, y);
    }

    public void setType(Material type) {
        setTypeId(type.getId());
    }

    public boolean setTypeId(int type) {
        return setTypeId(type, true);
    }

    public boolean setTypeId(int type, boolean applyPhysics) {
        return setTypeIdAndData(type, (byte) 0, applyPhysics);
    }

    public boolean setTypeIdAndData(int type, byte data, boolean applyPhysics) {
        chunk.setType(x & 0xf, z & 0xf, y, type);
        chunk.setMetaData(x & 0xf, z & 0xf, y, data);
        if (applyPhysics) {
            // todo: physics
        }
        BlockChangeMessage bcmsg = new BlockChangeMessage(x, y, z, type, data);
        for (GlowPlayer p : getWorld().getRawPlayers()) {
            p.sendBlockChange(bcmsg);
        }

        return true;
    }

    public boolean isEmpty() {
        return getTypeId() == 0;
    }

    public boolean isLiquid() {
        Material mat = getType();
        return mat == Material.WATER || mat == Material.STATIONARY_WATER || mat == Material.LAVA || mat == Material.STATIONARY_LAVA;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Data and light getters/setters

    public byte getData() {
        return (byte) chunk.getMetaData(x & 0xf, z & 0xf, y);
    }

    public void setData(byte data) {
        setData(data, true);
    }

    public void setData(byte data, boolean applyPhyiscs) {
        chunk.setMetaData(x & 0xf, z & 0xf, y & 0x7f, data);
        if (applyPhyiscs) {
            // todo: physics
        }
        BlockChangeMessage bcmsg = new BlockChangeMessage(x, y, z, getTypeId(), data);
        for (GlowPlayer p : getWorld().getRawPlayers()) {
            p.sendBlockChange(bcmsg);
        }
    }

    public byte getLightLevel() {
        return (byte) Math.max(getLightFromSky(), getLightFromBlocks());
    }

    public byte getLightFromSky() {
        return chunk.getSkyLight(x & 0xf, z & 0xf, y);
    }

    public byte getLightFromBlocks() {
        return chunk.getBlockLight(x & 0xf, z & 0xf, y);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Redstone

    public boolean isBlockPowered() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isBlockIndirectlyPowered() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isBlockFacePowered(BlockFace face) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isBlockFaceIndirectlyPowered(BlockFace face) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getBlockPower(BlockFace face) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getBlockPower() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PistonMoveReaction getPistonMoveReaction() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return "GlowBlock{chunk=" + getChunk() + ",x=" + x + ",y=" + y + ",z=" + z + ",type=" + getType() + ",data=" + getData() + "}";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Drops and breaking

    public boolean breakNaturally() {
        if (getType() == Material.AIR) {
            return false;
        }

        Location location = getLocation();
        for (ItemStack stack : getDrops()) {
            getWorld().dropItemNaturally(location, stack);
        }

        setType(Material.AIR);
        return true;
    }

    public boolean breakNaturally(ItemStack tool) {
        if (givesDrops(tool)) {
            return breakNaturally();
        } else {
            return setTypeId(Material.AIR.getId());
        }
    }

    public Collection<ItemStack> getDrops() {
        return ItemTable.instance().getBlock(getType()).getDrops(this);
    }

    public Collection<ItemStack> getDrops(ItemStack tool) {
        if (givesDrops(tool)) {
            return getDrops();
        } else {
            return Collections.emptyList();
        }
    }

    private boolean givesDrops(ItemStack tool) {
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Metadata

    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        metadata.setMetadata(this, metadataKey, newMetadataValue);
    }

    public List<MetadataValue> getMetadata(String metadataKey) {
        return metadata.getMetadata(this, metadataKey);
    }

    public boolean hasMetadata(String metadataKey) {
        return metadata.hasMetadata(this, metadataKey);
    }

    public void removeMetadata(String metadataKey, Plugin owningPlugin) {
        metadata.removeMetadata(this, metadataKey, owningPlugin);
    }
}
