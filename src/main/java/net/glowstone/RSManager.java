package net.glowstone;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.ItemTable;
import net.glowstone.block.blocktype.BlockType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import java.util.*;

/**
 * A class to manage redstone logic for a world.
 * @author Ben Russell
 */
public class RSManager {
    /**
     * A flag used to enable testing redstone by spamming the console.
     * Best to only enable this if you are a developer.
     */
    public static final boolean DEBUG_REDSTONE = false;

    /**
     * An immutable class for storing 3D world positions.
     */
    //TODO: Where should this class be moved to?
    public final class RSPos {
        public final int x, y, z;

        public RSPos(Block block) {
            this(block.getX(), block.getY(), block.getZ());
        }

        public RSPos(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public int hashCode() {
            int hash = 1;

            hash = hash * 31 + this.y;
            hash = hash * 53 + this.x;
            hash = hash * 19 + this.z;

            return hash;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof RSPos) {
                RSPos op = (RSPos)o;

                return true
                    && op.x == this.x
                    && op.y == this.y
                    && op.z == this.z;

            }

            return false;
        }

        @Override
        public String toString() {
            return String.format("RSPos(%d, %d, %d)", x, y, z);
        }
    }

    /**
     * The world this manager is assigned to.
     */
    private GlowWorld world;

    /**
     * Various redstone-related sets, maps and lists.
     */ 
    private Map<RSPos,Integer> redPowerNew = new HashMap<>();
    private Map<RSPos,Integer> redPowerOld = new HashMap<>();
    private Map<RSPos,Integer> redPowerRead = redPowerNew;
    private Set<RSPos> redPowerFlush = new HashSet<>();
    private Set<RSPos> redSourceNew = new HashSet<>();
    private Set<RSPos> redSourceOld = new HashSet<>();
    private Set<RSPos> chunksDirtyNew = new HashSet<>();
    private Set<RSPos> chunksDirtyOld = new HashSet<>();
    private Set<RSPos> blocksDirtyNew = new HashSet<>();
    private Set<RSPos> blocksDirtyOld = new HashSet<>();

    /**
     * Create a new RS manager for a world.
     * @param world The world this manager pertains to
     */
    public RSManager(GlowWorld world) {
        this.world = world;
    }

    /**
     * Sets the charge of a given block.
     * @param x
     * @param y
     * @param z
     * @param charge The charge level to set this block to.
     * @param oneTick Flag to indicate if we must discharge this block in the next tick.
     */
    public synchronized void setBlockPower(int x, int y, int z, int charge, boolean oneTick) {
        assert(charge >= 0 && charge <= 15);
        RSPos p = new RSPos(x, y, z);
        redPowerNew.put(p, (Integer)((charge&15) | (oneTick?0x20:0x00)));
    }

    /**
     * Sets the charge of a given block.
     * @param block The block to set the charge of.
     * @param charge The charge level to set this block to.
     * @param oneTick Flag to indicate if we must discharge this block in the next tick.
     */
    public void setBlockPower(Block block, int charge, boolean oneTick) {
        setBlockPower(block.getX(), block.getY(), block.getZ(), charge, oneTick);
    }

    /**
     * Sets the charge of a given block.
     * @param p The position of the block to set the charge of.
     * @param charge The charge level to set this block to.
     * @param oneTick Flag to indicate if we must discharge this block in the next tick.
     */
    public void setBlockPower(RSPos p, int charge, boolean oneTick) {
        setBlockPower(p.x, p.y, p.z, charge, oneTick);
    }

    /**
     * Gets the charge of a given block.
     * @param x
     * @param y
     * @param z
     * @return Power level in the range [0, 15].
     */
    public synchronized int getBlockPower(int x, int y, int z) {
        RSPos p = new RSPos(x, y, z);
        Integer charge = redPowerRead.get(p);
        return (charge == null ? 0 : ((int)charge) & 15);
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
        RSPos p = new RSPos(x, y, z);
        Integer charge = redPowerNew.get(p);
        return (charge == null ? 0 : ((int)charge) & 15);
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
        if(srcBlock == null) {
            return;
        }
        if(destBlock == null) {
            return;
        }
        
        // Get source material
        Material srcMat = srcBlock.getType();
        if(srcMat == null) {
            return;
        }

        // Get destination block data
        Material destMat = destBlock.getType();
        if(destMat == null) {
            return;
        }
        BlockType destType = ItemTable.instance().getBlock(destMat);
        if(destType == null) {
            return;
        }

        // Perform trace routine
        if(DEBUG_REDSTONE) {
            System.out.println(String.format("trace to %s", destBlock));
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
        if(destBlock == null) {
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
        RSPos p = new RSPos(block);
        redSourceNew.add(p);
        if(DEBUG_REDSTONE) {
            System.out.println(String.format("adding source %s", block));
        }
    }

    /**
     * Drops a redstone chunk out of the network.
     * @param chunkX
     * @param chunkZ
     */
    public synchronized void dropChunk(int chunkX, int chunkZ) {
        RSPos cp = new RSPos(chunkX, 0, chunkZ);

        if(DEBUG_REDSTONE) {
            System.out.println(String.format("red chunk !DROP! %d %d", chunkX, chunkZ));
        }

        // Loop
        for(int y = 0; y < 256; y++) {
            for(int z = 0; z < 16; z++) {
                for(int x = 0; x < 16; x++) {
                    // Get position
                    int rx = x + (chunkX << 4);
                    int ry = y;
                    int rz = z + (chunkZ << 4);
                    RSPos p = new RSPos(rx, ry, rz);

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
        blocksDirtyNew.add(new RSPos(x, y, z));
    }

    /**
     * Marks a chunk as dirty so the redstone manager can update it.
     * @param chunkX
     * @param chunkZ
     */
    public synchronized void dirtyChunk(int chunkX, int chunkZ) {
        chunksDirtyNew.add(new RSPos(chunkX, 0, chunkZ));
    }

    /**
     * Updates the redstone state for a block.
     * @param x
     * @param y
     * @param z
     */
    private synchronized void updateBlock(int x, int y, int z) {
        if(DEBUG_REDSTONE) {
            System.out.println(String.format("red block update %d %d %d", x, y, z));
        }

        // Get block
        GlowBlock block = world.getBlockAt(x, y, z);
        if(block == null) {
            return;
        }
        Material mat = block.getType();
        if(mat == null) {
            return;
        }
        BlockType type = ItemTable.instance().getBlock(mat);

        // Check if we can add this as a source
        if(type != null && type.isRedSource(block)) {
            addSource(block);
        }
    }

    /**
     * Updates the redstone state for a chunk.
     * @param chunkX
     * @param chunkZ
     */
    private synchronized void updateChunk(int chunkX, int chunkZ) {
        if(DEBUG_REDSTONE) {
            //System.out.println(String.format("red chunk update %d %d", chunkX, chunkZ));
        }

        // Fetch chunk
        GlowChunk chunk = world.getChunkAt(chunkX, chunkZ);

        // Loop
        for(int pos = 0, y = 0; y < 256; y++) {
            for(int z = 0; z < 16; z++) {
                for(int x = 0; x < 16; x++, pos++) {
                    // Get block
                    GlowBlock block = chunk.getBlock(x, y, z);
                    if(block == null) {
                        continue;
                    }
                    Material mat = block.getType();
                    if(mat == null) {
                        continue;
                    }
                    BlockType type = ItemTable.instance().getBlock(mat);

                    // Check if we can add this as a source
                    if(type != null && type.isRedSource(block)) {
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
     * Update all the redstone logic in the manager's assigned world.
     */
    public synchronized void pulse() {
        if(DEBUG_REDSTONE) {
            //System.out.println("RSManager pulse() start");
        }

        // Swap chunksDirty buffers
        Set<RSPos> chunksDirtyTemp = chunksDirtyNew;
        chunksDirtyNew = chunksDirtyOld;
        chunksDirtyOld = chunksDirtyTemp;

        // Clear dirty chunks
        chunksDirtyNew.clear();

        // Update all dirty chunks
        for(RSPos p : chunksDirtyOld) {
            // Skip unloaded chunks
            if(!isChunkLoaded(p.x, p.z)) {
                continue;
            }

            // Update RS chunk
            updateChunk(p.x, p.z);
        }

        // Swap blocksDirty buffers
        Set<RSPos> blocksDirtyTemp = blocksDirtyNew;
        blocksDirtyNew = blocksDirtyOld;
        blocksDirtyOld = blocksDirtyTemp;

        // Clear dirty blocks
        blocksDirtyNew.clear();

        // Update all dirty blocks
        for(RSPos p : blocksDirtyOld) {
            // Skip unloaded chunks
            if(!isChunkLoaded(p.x>>4, p.z>>4)) {
                continue;
            }

            // Update RS block
            updateBlock(p.x, p.y, p.z);
        }

        // Swap sources and clear new
        Set<RSPos> redSourceTemp = redSourceNew;
        redSourceNew = redSourceOld;
        redSourceOld = redSourceTemp;
        redSourceNew.clear();

        // Swap charges and clear new
        Map<RSPos,Integer> redPowerTemp = redPowerNew;
        redPowerNew = redPowerOld;
        redPowerOld = redPowerTemp;
        redPowerNew.clear();

        // Initialise sources
        for(RSPos p : redSourceOld) {
            GlowBlock block = world.getBlockAt(p.x, p.y, p.z);
            if(block == null) {
                continue;
            }
            Material mat = block.getType();
            if(mat == null) {
                continue;
            }
            BlockType type = ItemTable.instance().getBlock(mat);
            if(type == null) {
                continue;
            }
            type.traceBlockPowerInit(block, this);
        }

        // Trace from sources
        for(RSPos p : redSourceOld) {
            GlowBlock block = world.getBlockAt(p.x, p.y, p.z);
            if(block == null) {
                continue;
            }
            Material mat = block.getType();
            if(mat == null) {
                continue;
            }
            BlockType type = ItemTable.instance().getBlock(mat);
            if(type == null) {
                continue;
            }
            type.traceBlockPowerStart(block, this);
        }

        // Change read query to point to new charge
        redPowerRead = redPowerNew;

        // Handle old wire charges
        for(RSPos p : redPowerOld.keySet()) {
            if(!redPowerNew.containsKey(p)) {
                GlowBlock block = world.getBlockAt(p.x, p.y, p.z);
                if(block == null) {
                    continue;
                }

                int charge = redPowerOld.get(p);
                boolean oneTick = ((charge & 0x20) != 0);
                charge &= 0x0F;

                // Determine whether we discharge now or let it be
                if(oneTick) {
                    if(charge > 0) {
                        Material mat = block.getType();
                        if(mat == null) {
                            continue;
                        }
                        BlockType type = ItemTable.instance().getBlock(mat);
                        if(type == null) {
                            continue;
                        }
                        type.traceBlockPowerEnd(block, this, 0);
                    }
                } else {
                    redPowerNew.put(p, charge);
                }
            }
        }

        // Clear flush list
        redPowerFlush.clear();

        // Power new wires
        for(RSPos p : redPowerNew.keySet()) {
            GlowBlock block = world.getBlockAt(p.x, p.y, p.z);
            if(block == null) {
                // Destroy to prevent latent charges
                redPowerFlush.add(p);
                continue;
            }

            Material mat = block.getType();
            if(mat == null) {
                // Destroy to prevent latent charges
                redPowerFlush.add(p);
                continue;
            }
            BlockType type = ItemTable.instance().getBlock(mat);
            if(type == null) {
                // Destroy to prevent latent charges
                redPowerFlush.add(p);
                continue;
            }

            int charge = redPowerNew.get(p);
            type.traceBlockPowerEnd(block, this, charge & 0x0F);
        }

        // Flush latent charges
        for(RSPos p : redPowerFlush) {
            redPowerNew.remove(p);
        }
    }
}
