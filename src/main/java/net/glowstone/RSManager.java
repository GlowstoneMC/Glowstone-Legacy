package net.glowstone;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import java.util.*;

/**
 * A class to manage redstone logic for a world.
 * @author Ben Russell
 */
public class RSManager {
    /**
     * An enum of block types.
     */
    // TODO: Change this to use BlockType instead
    public enum RSBlock {
        B_AIR,
        B_SOLID,
        B_RWIRE,
        B_RTORCH,
        B_RDIODE, // Not supported yet
    }

    /**
     * An immutable class for storing 3D world positions.
     * TODO: Consider using Location (assuming it's hashable to the nearest int^3)
     */
    public final class RSPos {
        public final int x, y, z;

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
    private Set<RSPos> redTorches = new HashSet<RSPos>();
    private Set<RSPos> redSources = new HashSet<RSPos>();
    private List<RSPos> redChargedWires = new ArrayList<RSPos>();
    private Set<RSPos> redSinks = new HashSet<RSPos>();
    private Set<RSPos> chunksDirty = new HashSet<RSPos>();
    private Set<RSPos> chunksDirtyBack = new HashSet<RSPos>();

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
     * @param charged Whether this block is charged or not
     */
    private void setBlockCharge(int x, int y, int z, boolean charged) {
        // TODO!
    }

    /**
     * Drops a redstone chunk out of the network.
     * @param chunkX
     * @param chunkZ
     */
    public synchronized void dropRedChunk(int chunkX, int chunkZ) {
        // TODO: Check if we need to prevent double-removals of a chunk
        RSPos cp = new RSPos(chunkX, 0, chunkZ);

        //System.out.println(String.format("red chunk !DROP! %d %d", chunkX, chunkZ));

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
            redTorches.remove(p);
            redSources.remove(p);
            redSinks.remove(p);
        }
    }

    /**
     * Marks a chunk as dirty so the redstone manager can update it.
     * @param chunkX
     * @param chunkZ
     */
    public synchronized void dirtyRedChunk(int chunkX, int chunkZ) {
        chunksDirty.add(new RSPos(chunkX, 0, chunkZ));
    }

    /**
     * Updates the redstone state for a chunk.
     * @param chunkX
     * @param chunkZ
     */
    private synchronized void updateRedChunk(int chunkX, int chunkZ) {
        //System.out.println(String.format("red chunk update %d %d", chunkX, chunkZ));
        RSPos cp = new RSPos(chunkX, 0, chunkZ);

        // Fetch IDs and metadata
        // Loop
        for(int pos = 0, y = 0; y < 256; y++)
        for(int z = 0; z < 16; z++)
        for(int x = 0; x < 16; x++, pos++) {
            // Get type from chunk
            // TODO: Source from world
            RSBlock type = B_AIR;
            int meta = 0x00;
            boolean charged = false;

            // Get position
            int rx = x + (chunkX << 4);
            int ry = y;
            int rz = z + (chunkZ << 4);
            RSPos p = new RSPos(rx, ry, rz);

            // Remove this from all appropriate sets
            redTorches.remove(p);
            redSources.remove(p);
            redSinks.remove(p);

            switch(type) {
                case B_AIR:
                    // Air is never a source
                    break;
                case B_RWIRE:
                    // Wire isn't actually used at this step
                    break;
                case B_RTORCH:
                    // Add this to the torches AND charges AND sources
                    p = new RSPos(rx, ry, rz);
                    redTorches.add(p);
                    if(!charged) redSources.add(p);
                    break;
                case B_SOLID:
                    // Add this to the charges AND sources
                    p = new RSPos(rx, ry, rz);
                    if(charged) redSources.add(p);
                    break;
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
        // Swap chunksDirty buffers
        HashSet<RSPos> cdtemp = chunksDirty;
        chunksDirty = chunksDirtyBack;
        chunksDirtyBack = cdtemp;

        // Clear dirty chunks
        chunksDirty.clear();

        // Update all dirty chunks
        for(RSPos p : chunksDirtyBack) {
            // Skip unloaded chunks
            if(!isChunkLoaded(p.x, p.z)) continue;

            // Update RS chunk
            updateRedChunk(p.x, p.z);
        }

        // Clear sinks
        redSinks.clear();

        // Discharge wires
        for(RSPos p : redChargedWires)
            setBlockCharge(p.x, p.y, p.z, false);

        redChargedWires.clear();

        // Lead sources to sinks
        // TODO: Integrate properly
        /*
        for(RSPos p : redSources)
            traceRedStart(p.x, p.y, p.z);
        */

        // Discharge sources
        for(RSPos p : redSources)
            setBlockCharge(p.x, p.y, p.z, false);

        // Charge sinks
        for(RSPos p : redSinks)
            setBlockCharge(p.x, p.y, p.z, true);

        // Charge wires
        for(RSPos p : redChargedWires)
            setBlockCharge(p.x, p.y, p.z, true);

        // Flip sinks for torches
        for(RSPos p : redTorches) {
            if(redSinks.contains(p))
                redSinks.remove(p);
            else
                redSinks.add(p);
        }

        // Swap RS sources and sinks
        HashSet<RSPos> rstemp = redSources;
        redSources = redSinks;
        redSinks = rstemp;
    }
}
