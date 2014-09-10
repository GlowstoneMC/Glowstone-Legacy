package net.glowstone.block.blocktype;

import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class BlockRedstoneDust extends BlockType {
    // TODO: handle properly

    @Override
    public boolean isBlockEmittingPower(GlowBlock block, BlockFace face, boolean isDirect) {
        // RS wire does not emit directly
        if(isDirect) {
            return false;
        }

        return true;
    }

    @Override
    public void traceBlockPowerInit(GlowBlock block, RSManager rsManager) {
        // XXX: Do we discharge here, or rely on rsManager?
    }

    @Override
    public void traceBlockPowerStart(GlowBlock block, RSManager rsManager) {
        // This does not trace from the "start".
    }

    private void traceBlockPowerInject(GlowBlock block, RSManager rsManager, BlockFace outDir, int outPower) {
        rsManager.traceFromBlock(block, outDir, outPower, false);
    }

    private boolean traceBlockPowerRSWire(GlowBlock block, RSManager rsManager, BlockFace blockDir, BlockFace outDir, int outPower, boolean isDirect) {
        // Bail out early
        if(blockDir == outDir) {
            return false;
        }
        if(outPower <= 0) {
            return false;
        }

        // Get the relevant blocks + materials
        GlowBlock blockOn   = block.getRelative(BlockFace.UP);
        GlowBlock blockMid  = block.getRelative(outDir.getModX(), outDir.getModY() + 0, outDir.getModZ());
        GlowBlock blockUp   = block.getRelative(outDir.getModX(), outDir.getModY() + 1, outDir.getModZ());
        GlowBlock blockDown = block.getRelative(outDir.getModX(), outDir.getModY() - 1, outDir.getModZ());
        GlowBlock blockSeat = block.getRelative(BlockFace.DOWN);

        // Get some flags
        boolean wireMid  = (blockMid  != null && blockMid .getType() == Material.REDSTONE_WIRE);
        boolean wireUp   = (blockUp   != null && blockUp  .getType() == Material.REDSTONE_WIRE);
        boolean wireDown = (blockDown != null && blockDown.getType() == Material.REDSTONE_WIRE);

        boolean solidOn   = (blockOn   != null && blockOn  .getType().isOccluding());
        boolean solidMid  = (blockMid  != null && blockMid .getType().isOccluding());
        boolean solidUp   = (blockUp   != null && blockUp  .getType().isOccluding());
        boolean solidDown = (blockDown != null && blockDown.getType().isOccluding());

        // Check if glowstone 
        boolean glowOn    = (blockOn   != null && blockOn  .getType() == Material.GLOWSTONE);
        if(glowOn) {
            solidOn = false;
        }
        boolean glowSeat  = (blockSeat != null && blockSeat.getType() == Material.GLOWSTONE);
        if(glowSeat) {
            wireDown = false;
        }

        // Determine which one we use
        GlowBlock useBlock = null;
        if(wireDown && !solidMid) {
            // Down
            useBlock = blockDown;
            rsManager.traceFromBlockToBlock(block, useBlock, outDir, outPower, isDirect);
        }
        if(wireUp && !(solidOn && solidMid)) {
            // Up
            useBlock = blockUp;
            rsManager.traceFromBlockToBlock(block, useBlock, outDir, outPower, isDirect);
            if(glowOn) {
                // Trace upwards, too
                GlowBlock blockOn2  = blockOn.getRelative(BlockFace.UP);
                boolean wireOn2  = (blockOn2  != null && blockOn2 .getType() == Material.REDSTONE_WIRE);
                if(wireOn2) {
                    // Trace backwards
                    traceBlockPowerRSWire(blockUp, rsManager, BlockFace.SELF, outDir.getOppositeFace(), outPower-1, isDirect);
                }
            }
        }
        if(wireMid) {
            // Mid
            useBlock = blockMid;
            rsManager.traceFromBlockToBlock(block, useBlock, outDir, outPower, isDirect);
        }

        // Return if we had a block
        return (useBlock != null);
    }

    @Override
    public void traceBlockPower(GlowBlock block, RSManager rsManager, Material srcMat, BlockFace flowDir, int inPower, boolean isDirect) {
        // Bail out if our input power is <= our current power
        if(inPower <= rsManager.getNewBlockPower(block)) {
            return;
        }

        // Set power
        rsManager.setBlockPower(block, inPower, true);

        // Check if power sufficient
        if(inPower <= 1) {
            return;
        }

        // Spread out
        BlockFace blockDir = flowDir.getOppositeFace();
        int outPower = inPower - 1;
        boolean nn = traceBlockPowerRSWire(block, rsManager, blockDir, BlockFace.NORTH, outPower, false);
        boolean ns = traceBlockPowerRSWire(block, rsManager, blockDir, BlockFace.SOUTH, outPower, false);
        boolean ne = traceBlockPowerRSWire(block, rsManager, blockDir, BlockFace.EAST , outPower, false);
        boolean nw = traceBlockPowerRSWire(block, rsManager, blockDir, BlockFace.WEST , outPower, false);

        // Sum the numbers
        int bsum = 0;
        if(nn) { bsum++; }
        if(ns) { bsum++; }
        if(ne) { bsum++; }
        if(nw) { bsum++; }

        // If there is exactly one wire (the one we came from!), "inject"
        if(bsum == 0) {
            if(flowDir == BlockFace.UP || flowDir == BlockFace.DOWN) {
                // Special case
                traceBlockPowerInject(block, rsManager, BlockFace.NORTH, outPower);
                traceBlockPowerInject(block, rsManager, BlockFace.SOUTH, outPower);
                traceBlockPowerInject(block, rsManager, BlockFace.WEST, outPower);
                traceBlockPowerInject(block, rsManager, BlockFace.EAST, outPower);
            } else {
                traceBlockPowerInject(block, rsManager, flowDir, outPower);
            }
        }

        // Move to floor
        rsManager.traceFromBlockIfUnpowered(block, BlockFace.DOWN, outPower, false);
    }

    @Override
    public void traceBlockPowerEnd(GlowBlock block, RSManager rsManager, int power) {
        // Set block charge
        assert(power >= 0 && power <= 15);
        block.setTypeIdAndData(getMaterial().getId(), (byte)(power & 15), false);
    }

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        GlowBlock floor = block.getRelative(BlockFace.DOWN);
        if(floor != null) {
            Material mat = floor.getType();
            if(mat.isOccluding()) {
                return true;
            }
            if(mat == Material.GLOWSTONE) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.REDSTONE, 1, (short)0)));
    }
} 
