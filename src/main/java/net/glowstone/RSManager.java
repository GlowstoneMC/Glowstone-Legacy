package net.glowstone;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import java.util.*;
import org.bukkit.util.BlockVector;

/**
 * A class to manage redstone logic for a world.
 * @author Ben Russell
 */
public class RSManager {

    /**
     * The world this manager is assigned to.
     */
    private final GlowWorld world;

    /**
     * Various redstone-related sets, maps and lists.
     */ 
    private final Map<BlockVector, Integer> redPowerNew = new HashMap<>();
    private Set<BlockVector> redPowerOld = new HashSet<>();
    private Set<BlockVector> redPowerRemoval = new HashSet<>();
    private final Map<BlockVector, Integer> redPowerCurrent = new HashMap<>();
    private final Map<BlockVector, Integer> redPowerWait = new HashMap<>();
    private final Set<BlockVector> redFlush = new HashSet<>(); 
    private Set<BlockVector> redSourceNew = new HashSet<>();
    private Set<BlockVector> redSourceOld = new HashSet<>();
    private Set<BlockVector> chunksDirtyNew = new HashSet<>();
    private Set<BlockVector> chunksDirtyOld = new HashSet<>();
    private Set<BlockVector> blocksDirtyNew = new HashSet<>();
    private Set<BlockVector> blocksDirtyOld = new HashSet<>();

    /**
     * Create a new RS manager for a world.
     * @param world The world this manager pertains to
     */
    public RSManager(GlowWorld world) {
        this.world = world;
    }

    public void addFlush(GlowBlock block) {
        redFlush.add(new BlockVector(block.getX(), block.getY(), block.getZ()));
    }

    /**
     * Sets the charge of a given block after a given delay.
     * @param x
     * @param y
     * @param z
     * @param charge The charge level to set this block to.
     * @param afterTicks The ticks to wait after we set the given charge.
     */
    public synchronized void setBlockPowerDelayed(int x, int y, int z, int charge, int afterTicks) {
        assert (charge >= 0 && charge <= 15);
        BlockVector p = new BlockVector(x, y, z);
        redPowerWait.put(p, afterTicks);
        redPowerNew.put(p, charge);
    }

    /**
     * Sets the charge of a given block after a given delay.
     * @param block The block to set the charge of.
     * @param charge The charge level to set this block to.
     * @param afterTicks The ticks to wait after we set the given charge.
     */
    public void setBlockPowerDelayed(Block block, int charge, int afterTicks) {
        setBlockPowerDelayed(block.getX(), block.getY(), block.getZ(), charge, afterTicks);
    }

    /**
     * Sets the charge of a given block after a given delay.
     * @param p The position of the block to set the charge of.
     * @param charge The charge level to set this block to.
     * @param afterTicks The ticks to wait after we set the given charge.
     */
    public void setBlockPowerDelayed(BlockVector p, int charge, int afterTicks) {
        setBlockPowerDelayed(p.getBlockX(), p.getBlockY(), p.getBlockZ(), charge, afterTicks);
    }

    public void removeBlockPowerDelay(Block block) {
        removeBlockPowerDelay(block.getX(), block.getY(), block.getZ());
    }

    public void removeBlockPowerDelay(BlockVector p) {
        redPowerWait.remove(p);
        redPowerNew.remove(p);
    }

    public void removeBlockPowerDelay(int x, int y, int z) {
        removeBlockPowerDelay(new BlockVector(x, y, z));
    }

    /**
     * Sets the charge of a given block.
     * @param x
     * @param y
     * @param z
     * @param charge The charge level to set this block to.
     */
    public synchronized void setBlockPower(int x, int y, int z, int charge) {
        assert (charge >= 0 && charge <= 15);
        BlockVector p = new BlockVector(x, y, z);
        redPowerCurrent.put(p, charge);
    }

    /**
     * Sets the charge of a given block.
     * @param block The block to set the charge of.
     * @param charge The charge level to set this block to.
     */
    public void setBlockPower(Block block, int charge) {
        setBlockPower(block.getX(), block.getY(), block.getZ(), charge);
    }

    /**
     * Sets the charge of a given block.
     * @param p The position of the block to set the charge of.
     * @param charge The charge level to set this block to.
     */
    public void setBlockPower(BlockVector p, int charge) {
        setBlockPower(p.getBlockX(), p.getBlockY(), p.getBlockZ(), charge);
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
        Integer charge = type.getBlockPower(block); //For some blocks redPowerCurrent may not be correct
        if (charge == null) {
            BlockVector p = new BlockVector(x, y, z);
            charge = redPowerCurrent.get(p);
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
     * Gets the to-be-applied charge of a given block.
     * @param x
     * @param y
     * @param z
     * @return Power level in the range [0, 15].
     */
    public synchronized int getNewBlockPower(int x, int y, int z) {
        BlockVector p = new BlockVector(x, y, z);
        Integer charge = redPowerNew.get(p);
        return (charge == null ? -1 : charge);
    }

    /**
     * Gets the to-be-applied charge of a given block.
     * @param block The block to get the charge of.
     * @return Power level in the range [0, 15].
     */
    public int getNewBlockPower(Block block) {
        return getNewBlockPower(block.getX(), block.getY(), block.getZ());
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
        redSourceNew.add(p);
    }

    /**
     * Drops a redstone chunk out of the network.
     * @param chunkX
     * @param chunkZ
     */
    public synchronized void dropChunk(int chunkX, int chunkZ) {
        // Loop
        for (int y = 0; y < 256; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++) {
                    // Get position
                    int rx = x + (chunkX << 4);
                    int ry = y;
                    int rz = z + (chunkZ << 4);
                    BlockVector p = new BlockVector(rx, ry, rz);

                    // Remove this from all appropriate sets
                    redPowerNew.remove(p);
                    redSourceNew.remove(p);
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
        BlockVector bv = new BlockVector(x, y, z);
        blocksDirtyNew.add(bv);
    }

    /**
     * Marks a chunk as dirty so the redstone manager can update it.
     * @param chunkX
     * @param chunkZ
     */
    public synchronized void dirtyChunk(int chunkX, int chunkZ) {
        chunksDirtyNew.add(new BlockVector(chunkX, 0, chunkZ));
    }

    /**
     * Updates the redstone state for a block.
     * @param x
     * @param y
     * @param z
     */
    private synchronized void updateBlock(int x, int y, int z) {
        // Get block
        GlowBlock block = world.getBlockAt(x, y, z);
        if (block == null) {
            return;
        }
        Material mat = block.getType();
        if (mat == null) {
            return;
        }
        BlockType type = ItemTable.instance().getBlock(mat);
        // Check if we can add this as a source
        if (type != null && type.isRedSource(block)) {
            addSource(block);
        }
    }

    /**
     * Updates the redstone state for a chunk.
     * @param chunkX
     * @param chunkZ
     */
    private synchronized void updateChunk(int chunkX, int chunkZ) {
        // Fetch chunk
        GlowChunk chunk = world.getChunkAt(chunkX, chunkZ);

        // Loop
        for (int pos = 0, y = 0; y < 256; y++) {
            for (int z = 0; z < 16; z++) {
                for (int x = 0; x < 16; x++, pos++) {
                    // Get block
                    GlowBlock block = chunk.getBlock(x, y, z);
                    if (block == null) {
                        continue;
                    }
                    Material mat = block.getType();
                    if (mat == null) {
                        continue;
                    }
                    BlockType type = ItemTable.instance().getBlock(mat);

                    // Check if we can add this as a source
                    if (type != null && type.isRedSource(block)) {
                        addSource(block);
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
        if (type == null) {
            return null;
        }
        return type;
    }

    /**
     * Update all the redstone logic in the manager's assigned world.
     */
    public synchronized void pulse() {
        // Swap chunksDirty buffers
        Set<BlockVector> chunksDirtyTemp = chunksDirtyNew;
        chunksDirtyNew = chunksDirtyOld;
        chunksDirtyOld = chunksDirtyTemp;

        // Clear dirty chunks
        chunksDirtyNew.clear();

        // Update all dirty chunks
        for (BlockVector p : chunksDirtyOld) {
            // Skip unloaded chunks
            if (!isChunkLoaded(p.getBlockX(), p.getBlockZ())) {
                continue;
            }

            // Update RS chunk
            updateChunk(p.getBlockX(), p.getBlockZ());
        }

        // Swap blocksDirty buffers
        Set<BlockVector> blocksDirtyTemp = blocksDirtyNew;
        blocksDirtyNew = blocksDirtyOld;
        blocksDirtyOld = blocksDirtyTemp;

        // Clear dirty blocks
        blocksDirtyNew.clear();

        // Update all dirty blocks
        for (BlockVector p : blocksDirtyOld) {
            // Skip unloaded chunks
            if (!isChunkLoaded(p.getBlockX() >> 4, p.getBlockZ() >> 4)) {
                continue;
            }

            // Update RS block
            updateBlock(p.getBlockX(), p.getBlockY(), p.getBlockZ());
        }

        // Swap sources and clear new
        Set<BlockVector> redSourceTemp = redSourceNew;
        redSourceNew = redSourceOld;
        redSourceOld = redSourceTemp;
        redSourceNew.clear();


        // Initialise sources
        for (BlockVector p : redSourceOld) {
            GlowBlock block = world.getBlockAt(p.getBlockX(), p.getBlockY(), p.getBlockZ());
            BlockType type = getBlockType(block);
            if (type == null) {
                continue;
            }
            type.traceBlockPowerInit(block, this);
        }

        // Trace from sources
        for (BlockVector p : redSourceOld) {
            GlowBlock block = world.getBlockAt(p.getBlockX(), p.getBlockY(), p.getBlockZ());
            BlockType type = getBlockType(block);
            if (type == null) {
                continue;
            }
            type.traceBlockPowerStart(block, this);
        }

        // Copy redPowerCurrent (and redPowerWait) blockvectors, otherwise we would get a ConcurrentModificationException
        Set<BlockVector> redPowerTemp = new HashSet<>(redPowerCurrent.keySet());
        redPowerTemp.addAll(new HashSet<>(redPowerWait.keySet()));
        for (BlockVector p : redPowerTemp) {
            redPowerOld.add(p);
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
            redPowerRemoval.remove(p);
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
        redPowerRemoval = redPowerOld;
        redPowerOld = new HashSet<>();
    }
}