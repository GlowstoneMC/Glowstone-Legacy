package net.glowstone;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.BlockVector;

/**
 * A class to manage redstone logic for a world.
 * @author Ben Russell, kaenganxt
 */
public class RSManager {

    /**
     * The world this manager is assigned to.
     */
    private final GlowWorld world;

    /**
     * Various redstone-related sets, maps and lists.
     */ 
    private final Map<BlockVector, Integer> redPowerCurrent = new HashMap<>();
    private Map<BlockVector, Integer> redPowerOld = new HashMap<>();
    private Set<BlockVector> redPowerRemoval = new HashSet<>();
    private final Set<BlockVector> redFlush = new HashSet<>(); 
    private final Map<BlockVector, Integer> redPowerWait = new HashMap<>();
    private final Map<BlockVector, Integer> redPowerNew = new HashMap<>();
    private final Set<BlockVector> redSource = new HashSet<>();
    private final Set<BlockVector> addAsSource = new HashSet<>();

    /**
     * Create a new RS manager for a world.
     * @param world The world this manager pertains to
     */
    public RSManager(GlowWorld world) {
        this.world = world;
    }

    /**
     * Removes all references of a block in the RSManager
     * @param block The block to remove references of
     */
    public void addFlush(GlowBlock block) {
        addFlush(new BlockVector(block.getX(), block.getY(), block.getZ()));
    }
    
    /**
     * Removes all references of a block in the RSManager
     * @param vector The BlockVector to remove references of
     */
    public void addFlush(BlockVector vector) {
        redFlush.add(vector);
    }

    /**
     * Sets the charge of a given block after a given delay.
     * @param bv The position of the block to set the charge of.
     * @param charge The charge level to set this block to.
     * @param afterTicks The ticks to wait after we set the given charge.
     */
    public synchronized void setBlockPowerDelayed(BlockVector bv, int charge, int afterTicks) {
        assert (charge >= 0 && charge <= 15);
        redPowerWait.put(bv, afterTicks);
        redPowerNew.put(bv, charge);
    }

    /**
     * Sets the charge of a given block after a given delay.
     * @param block The block to set the charge of.
     * @param charge The charge level to set this block to.
     * @param afterTicks The ticks to wait after we set the given charge.
     */
    public void setBlockPowerDelayed(Block block, int charge, int afterTicks) {
        setBlockPowerDelayed(new BlockVector(block.getX(), block.getY(), block.getZ()), charge, afterTicks);
    }

    /**
     * Removes the charge waiting to be set after a certain delay
     * @param block The block to remove the charge of
     */
    public void removeBlockPowerDelay(Block block) {
        removeBlockPowerDelay(new BlockVector(block.getX(), block.getY(), block.getZ()));
    }

    /**
     * Removes the charge waiting to be set after a certain delay
     * @param p The BlockVector to remove the charge of
     */
    public void removeBlockPowerDelay(BlockVector p) {
        redPowerWait.remove(p);
        redPowerNew.remove(p);
    }

    /**
     * Sets the charge of a given block.
     * @param bv The position of the block to set the charge of.
     * @param charge The charge level to set this block to.
     */
    public synchronized void setBlockPower(BlockVector bv, int charge) {
        assert (charge >= 0 && charge <= 15);
        redPowerCurrent.put(bv, charge);
    }

    /**
     * Sets the charge of a given block.
     * @param block The block to set the charge of.
     * @param charge The charge level to set this block to.
     */
    public void setBlockPower(Block block, int charge) {
        setBlockPower(new BlockVector(block.getX(), block.getY(), block.getZ()), charge);
    }

    /**
     * Gets the charge of a given block.
     * @param x
     * @param y
     * @param z
     * @return Power level in the range [0, 15].
     */
    public synchronized int getBlockPower(int x, int y, int z) {
        GlowBlock block = world.getBlockAt(x, y, z);
        BlockType type = getBlockType(block);
        if (type == null) return 0;
        Integer charge = type.getBlockPower(block); //For some blocks our value may not be correct
        if (charge == null) {
            BlockVector p = new BlockVector(x, y, z);
            charge = (redPowerCurrent.containsKey(p) ? redPowerCurrent.get(p) : redPowerOld.get(p));
        }
        return (charge == null ? 0 : ((int) charge) & 15);
    }

    /**
     * Gets the charge of a given block.
     * @param block The block to get the charge of.
     * @return Power level in the range [0, 15].
     */
    public int getBlockPower(Block block) { 
        return getBlockPower(block.getX(), block.getY(), block.getZ());
    }

    /**
     * Gets the current charge of a given block.
     * NOTE: Only returns the correct value during a redstone pulse
     * @param bv The BlockVector to get the charge of.
     * @return Power level in the range [0, 15].
     */
    public synchronized int getCurrentBlockPower(BlockVector bv) {
        return redPowerCurrent.containsKey(bv) ? redPowerCurrent.get(bv) : 0;
    }

    /**
     * Gets the current charge of a given block.
     * NOTE: Only returns the correct value during a redstone pulse
     * @param block The block to get the charge of.
     * @return Power level in the range [0, 15].
     */
    public int getCurrentBlockPower(Block block) { 
        return getCurrentBlockPower(new BlockVector(block.getX(), block.getY(), block.getZ()));
    }

    /**
     * Gets the to-be-applied charge of a given block.
     * @param bv The block to get the new power of.
     * @return Power level in the range [0, 15].
     */
    public synchronized int getNewBlockPower(BlockVector bv) {
        Integer charge = redPowerNew.get(bv);
        return (charge == null ? -1 : charge);
    }

    /**
     * Gets the to-be-applied charge of a given block.
     * @param block The block to get the charge of.
     * @return Power level in the range [0, 15].
     */
    public int getNewBlockPower(Block block) {
        return getNewBlockPower(new BlockVector(block.getX(), block.getY(), block.getZ()));
    }

    /**
     * Trace redstone current to a block from another block.
     * @param srcBlock The block we are sourcing from.
     * @param destBlock The block we are targeting.
     * @param flowDir The direction of redstone flow from the source block.
     * @param inPower The input power level.
     * @param isDirect Whether we are applying direct or indirect power.
     */
    public void traceFromBlockToBlock(GlowBlock srcBlock, GlowBlock destBlock, BlockFace flowDir, int inPower, boolean isDirect) {
        if (srcBlock == null) {
            return;
        }
        if (destBlock == null) {
            return;
        }

        // Get source material
        Material srcMat = srcBlock.getType();
        if (srcMat == null) {
            return;
        }

        // Get destination block data
        Material destMat = destBlock.getType();
        if (destMat == null) {
            return;
        }
        BlockType destType = ItemTable.instance().getBlock(destMat);
        if (destType == null) {
            return;
        }

        destType.traceBlockPower(destBlock, this, srcMat, flowDir, inPower, isDirect);
    }

    /**
     * Trace redstone current from a block to another block given a direction.
     * @param srcBlock The block we are sourcing from.
     * @param flowDir The direction of redstone flow from this block.
     * @param inPower The input power level.
     * @param isDirect Whether we are applying direct or indirect power.
     */
    public void traceFromBlock(GlowBlock srcBlock, BlockFace flowDir, int inPower, boolean isDirect) {
        // Get destination block
        GlowBlock destBlock = srcBlock.getRelative(flowDir);
        if (destBlock == null) {
            return;
        }

        // Move to block
        traceFromBlockToBlock(srcBlock, destBlock, flowDir, inPower, isDirect);
    }

    /**
     * Adds a source for redstone tracing updates.
     * @param block The block we use as a source.
     */
    public synchronized void addSource(Block block) {
        BlockVector p = new BlockVector(block.getX(), block.getY(), block.getZ());
        redSource.add(p);
    }

    /**
     * Drops a redstone chunk out of the network.
     * @param chunkX
     * @param chunkZ
     */
    public synchronized void dropChunk(int chunkX, int chunkZ) {
        for (int y = 0; y < 256; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    // Get position
                    int rx = x + (chunkX << 4);
                    int ry = y;
                    int rz = z + (chunkZ << 4);
                    BlockVector p = new BlockVector(rx, ry, rz);

                    // Remove this from all appropriate sets
                    addFlush(p);
                }
            }
        }
    }

    /**
     * Marks a block as dirty so the redstone manager can update it.
     * @param x
     * @param y
     * @param z
     */
    public synchronized void dirtyBlock(int x, int y, int z) {
        // Get block
        GlowBlock block = world.getBlockAt(x, y, z);
        BlockType type = getBlockType(block);
        // Check if we can add this as a source
        if (type != null && type.isRedSource(block)) {
            addAsSource.add(new BlockVector(block.getX(), block.getY(), block.getZ()));
        }
    }

    /**
     * Marks a chunk as dirty so the redstone manager can update it.
     * @param chunkX
     * @param chunkZ
     */
    public synchronized void dirtyChunk(int chunkX, int chunkZ) {
         // Fetch chunk
        GlowChunk chunk = world.getChunkAt(chunkX, chunkZ);
        // Loop
        for (int y = 0; y < 256; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    // Get block
                    GlowBlock block = chunk.getBlock(x, y, z);
                    BlockType type = getBlockType(block);
                    if (type != null && type.isRedSource(block)) {
                        addAsSource.add(new BlockVector(block.getX(), block.getY(), block.getZ()));
                    }
                }
            }
        }
    }

    /**
     * Determine if a given chunk is loaded or not.
     */
    private boolean isChunkLoaded(int x, int z) {
        return world.isChunkLoaded(x, z);
    }

    /**
     * Returns the block type of a given block and performs all necessary null checks.
     * @param block The block to return the block type of.
     * @return The BlockType of a given block.
     */
    private BlockType getBlockType(GlowBlock block) {
        if (block == null) {
            return null;
        }
        Material mat = block.getType();
        if (mat == null) {
            return null;
        }
        BlockType type = ItemTable.instance().getBlock(mat);
        return type;
    }

    /**
     * Update all the redstone logic in the manager's assigned world.
     */
    public synchronized void pulse() {

        // Swap blocksDirty buffers
        Set<BlockVector> addAsSourceTemp = new HashSet<>(addAsSource);

        // Update all dirty blocks
        for (BlockVector p : addAsSourceTemp) {
            // Skip unloaded chunks
            if (!isChunkLoaded(p.getBlockX() >> 4, p.getBlockZ() >> 4)) {
                continue;
            }
            redSource.add(p);
        }

        Set<BlockVector> redSourceTemp = new HashSet<>(redSource);

        // Initialise sources
        for (BlockVector p : redSourceTemp) {
            redSource.remove(p);
            GlowBlock block = world.getBlockAt(p.getBlockX(), p.getBlockY(), p.getBlockZ());
            BlockType type = getBlockType(block);
            if (type == null) {
                continue;
            }
            type.traceBlockPowerInit(block, this);
        }

        // Trace from sources
        for (BlockVector p : redSourceTemp) {
            GlowBlock block = world.getBlockAt(p.getBlockX(), p.getBlockY(), p.getBlockZ());
            BlockType type = getBlockType(block);
            if (type == null) {
                continue;
            }
            type.traceBlockPowerStart(block, this);
        }

        redPowerOld = new HashMap<>();
        // Copy redPowerCurrent (and redPowerWait) blockvectors, otherwise we would get a ConcurrentModificationException
        Set<BlockVector> redPowerTemp = new HashSet<>(redPowerCurrent.keySet());
        redPowerTemp.addAll(new HashSet<>(redPowerWait.keySet()));
        for (BlockVector p : redPowerTemp) {
            redPowerRemoval.remove(p);
            GlowBlock block = world.getBlockAt(p.getBlockX(), p.getBlockY(), p.getBlockZ());
            BlockType type = getBlockType(block);
            if (type == null) {
                continue;
            }
            int charge;
            if (!redPowerWait.containsKey(p)) {
                charge = (redPowerCurrent.containsKey(p) ? redPowerCurrent.get(p) : 0);
                redPowerCurrent.remove(p);
            } else if (redPowerWait.get(p) == 0) {
                charge = redPowerNew.get(p);
                redPowerWait.remove(p);
                redPowerCurrent.remove(p);
            } else {
                charge = (redPowerCurrent.containsKey(p) ? redPowerCurrent.get(p) : 0);
                redPowerWait.put(p, redPowerWait.get(p) - 1);
            }
            redPowerOld.put(p, charge);
            type.traceBlockPowerEnd(block, this, charge);
        }

        for (BlockVector p : redPowerCurrent.keySet()) {
            GlowBlock block = world.getBlockAt(p.getBlockX(), p.getBlockY(), p.getBlockZ());
            BlockType type = getBlockType(block);
            if (type == null) {
                // Destroy to prevent latent charges
                redFlush.add(p);
            }
        }

        // Flush latent charges
        for (BlockVector p : redFlush) {
            redPowerNew.remove(p);
            redPowerWait.remove(p);
            redPowerCurrent.remove(p);
            redPowerOld.remove(p);
        }
        redFlush.clear();

        for (BlockVector p : redPowerRemoval) {
            GlowBlock block = world.getBlockAt(p.getBlockX(), p.getBlockY(), p.getBlockZ());
            BlockType type = getBlockType(block);
            if (type == null) {
                continue;
            }
            type.traceBlockPowerEnd(block, this, 0);
        }
        redPowerRemoval = redPowerOld.keySet();
    }
}
