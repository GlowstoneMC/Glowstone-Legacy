package net.glowstone;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
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
    private Map<RSPos,Integer> redChargeNew = new HashMap<RSPos,Integer>();
    private Map<RSPos,Integer> redChargeOld = new HashMap<RSPos,Integer>();
    private Set<RSPos> redSourceNew = new HashSet<RSPos>();
    private Set<RSPos> redSourceOld = new HashSet<RSPos>();
    private Set<RSPos> chunksDirtyNew = new HashSet<RSPos>();
    private Set<RSPos> chunksDirtyOld = new HashSet<RSPos>();
    private Set<RSPos> blocksDirtyNew = new HashSet<RSPos>();
    private Set<RSPos> blocksDirtyOld = new HashSet<RSPos>();

    /**
     * Create a new RS manager for a world.
     * @param world The world this manager pertains to
     */
    public RSManager(GlowWorld world) {
        this.world = world;
        // TODO: Get a list of chunks and mark the loaded ones as dirty
    }

    /**
     * Sets the charge of a given block.
     * @param x
     * @param y
     * @param z
     * @param charge The charge level to set this block to.
     * @param oneTick Flag to indicate if we must discharge this block in the next tick.
     */
    public void setBlockCharge(int x, int y, int z, int charge, boolean oneTick) {
        assert(charge >= 0 && charge <= 15);
        RSPos p = new RSPos(x, y, z);
        redChargeNew.put(p, (Integer)((charge&15) | (oneTick?0x20:0x00)));
    }

    /**
     * Sets the charge of a given block.
     * @param block The block to set the charge of.
     * @param charge The charge level to set this block to.
     * @param oneTick Flag to indicate if we must discharge this block in the next tick.
     */
    public void setBlockCharge(Block block, int charge, boolean oneTick) {
        setBlockCharge(block.getX(), block.getY(), block.getZ(), charge, oneTick);
    }

    /**
     * Sets the charge of a given block.
     * @param p The position of the block to set the charge of.
     * @param charge The charge level to set this block to.
     * @param oneTick Flag to indicate if we must discharge this block in the next tick.
     */
    public void setBlockCharge(RSPos p, int charge, boolean oneTick) {
        setBlockCharge(p.x, p.y, p.z, charge, oneTick);
    }

    /**
     * Trace from a block to another block.
     * @param srcBlock The block we are sourcing from.
     * @param flowDir The direction of redstone flow from this block.
     * @param inPower The input power level.
     * @param isDirect Whether we are applying direct or indirect power.
     */
    public void traceFromBlock(GlowBlock srcBlock, BlockFace flowDir, int inPower, boolean isDirect) {
        // Get source material
        Material srcMat = srcBlock.getType();

        // Get destination block
        GlowBlock destBlock = srcBlock.getRelative(flowDir);
        if(destBlock == null) {
            return;
        }
        Material destMat = destBlock.getType();
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
     * Trace from a block, provided there is no power coming from the given direction.
     * @param srcBlock The block we are sourcing from.
     * @param flowDir The direction of redstone flow from this block.
     * @param inPower The input power level.
     * @param isDirect Whether we are applying direct or indirect power.
     */
    public void traceFromBlockIfUnpowered(GlowBlock srcBlock, BlockFace flowDir, int inPower, boolean isDirect) {
        // TODO: Check if destination is powered.
        // Perform trace.
        traceFromBlock(srcBlock, flowDir, inPower, isDirect);
    }

    /**
     * Adds a source for tracing updates.
     * @param block The block we use as a source.
     */
    public void addSource(Block block) {
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
    public synchronized void dropRedChunk(int chunkX, int chunkZ) {
        // TODO: Check if we need to prevent double-removals of a chunk
        RSPos cp = new RSPos(chunkX, 0, chunkZ);

        if(DEBUG_REDSTONE) {
            System.out.println(String.format("red chunk !DROP! %d %d", chunkX, chunkZ));
        }

        // Loop
        for(int y = 0; y < 256; y++)
        for(int z = 0; z < 16; z++)
        for(int x = 0; x < 16; x++) {
            // Get position
            int rx = x + (chunkX << 4);
            int ry = y;
            int rz = z + (chunkZ << 4);
            RSPos p = new RSPos(rx, ry, rz);

            // Remove this from all appropriate sets
            redChargeNew.remove(p);
            redSourceNew.remove(p);
        }
    }

    /**
     * Marks a block as dirty so the redstone manager can update it.
     * @param x
     * @param y
     * @param z
     */
    public synchronized void dirtyRedBlock(int x, int y, int z) {
        blocksDirtyNew.add(new RSPos(x, y, z));
    }

    /**
     * Marks a chunk as dirty so the redstone manager can update it.
     * @param chunkX
     * @param chunkZ
     */
    public synchronized void dirtyRedChunk(int chunkX, int chunkZ) {
        chunksDirtyNew.add(new RSPos(chunkX, 0, chunkZ));
    }

    /**
     * Updates the redstone state for a block.
     * @param x
     * @param y
     * @param z
     */
    private synchronized void updateRedBlock(int x, int y, int z) {
        if(DEBUG_REDSTONE) {
            System.out.println(String.format("red block update %d %d %d", x, y, z));
        }

        // Get block
        GlowBlock block = world.getBlockAt(x, y, z);
        if(block == null) {
            return;
        }
        BlockType type = ItemTable.instance().getBlock(block.getType());

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
    private synchronized void updateRedChunk(int chunkX, int chunkZ) {
        if(DEBUG_REDSTONE) {
            //System.out.println(String.format("red chunk update %d %d", chunkX, chunkZ));
        }

        // Fetch chunk
        GlowChunk chunk = world.getChunkAt(chunkX, chunkZ);

        // Loop
        for(int pos = 0, y = 0; y < 256; y++)
        for(int z = 0; z < 16; z++)
        for(int x = 0; x < 16; x++, pos++) {
            // Get block
            GlowBlock block = chunk.getBlock(x, y, z);
            if(block == null) {
                continue;
            }
            BlockType type = ItemTable.instance().getBlock(block.getType());

            // Check if we can add this as a source
            if(type != null && type.isRedSource(block)) {
                addSource(block);
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
            updateRedChunk(p.x, p.z);
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
            updateRedBlock(p.x, p.y, p.z);
        }

        // Swap sources and clear new
        Set<RSPos> redSourceTemp = redSourceNew;
        redSourceNew = redSourceOld;
        redSourceOld = redSourceTemp;
        redSourceNew.clear();

        // Swap charges and clear new
        Map<RSPos,Integer> redChargeTemp = redChargeNew;
        redChargeNew = redChargeOld;
        redChargeOld = redChargeTemp;
        redChargeNew.clear();

        // Initialise sources
        for(RSPos p : redSourceOld) {
            GlowBlock block = world.getBlockAt(p.x, p.y, p.z);
            if(block == null) {
                continue;
            }
            BlockType type = ItemTable.instance().getBlock(block.getType());
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
            BlockType type = ItemTable.instance().getBlock(block.getType());
            if(type == null) {
                continue;
            }
            type.traceBlockPowerStart(block, this);
        }

        // Handle old wire charges
        for(RSPos p : redChargeOld.keySet()) {
            if(!redChargeNew.containsKey(p)) {
                GlowBlock block = world.getBlockAt(p.x, p.y, p.z);
                if(block == null) {
                    continue;
                }

                int charge = redChargeOld.get(p);
                boolean oneTick = ((charge & 0x20) != 0);
                charge &= 0x0F;
                BlockType type = ItemTable.instance().getBlock(block.getType());
                if(type == null) {
                    continue;
                }

                // Determine whether we discharge now or let it be
                if(oneTick) {
                    if(charge > 0) {
                        type.traceBlockPowerEnd(block, this, 0);
                    }
                } else {
                    redChargeNew.put(p, charge);
                }
            }
        }

        // Charge new wires
        for(RSPos p : redChargeNew.keySet()) {
            GlowBlock block = world.getBlockAt(p.x, p.y, p.z);
            if(block == null) {
                continue;
            }

            int charge = redChargeNew.get(p);
            BlockType type = ItemTable.instance().getBlock(block.getType());
            if(type == null) {
                continue;
            }

            type.traceBlockPowerEnd(block, this, charge & 0x0F);
        }
    }
}
