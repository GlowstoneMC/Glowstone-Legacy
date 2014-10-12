package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;

public class BlockRedstoneDust extends BlockType {
    @Override
    public boolean canBlockEmitPower(GlowBlock block, BlockFace face, boolean isDirect) {
        // RS wire does not emit directly
        return !isDirect;
    }

    @Override
    public void traceBlockPowerInit(GlowBlock block, RSManager rsManager) {
    }

    @Override
    public void traceBlockPowerStart(GlowBlock block, RSManager rsManager) {
    }

    private void traceBlockPowerInject(GlowBlock block, RSManager rsManager, BlockFace outDir, int outPower) {
        rsManager.traceFromBlock(block, outDir, outPower, false);
    }

    private boolean traceBlockPowerRSWire(GlowBlock block, RSManager rsManager, BlockFace forbidDir, BlockFace outDir, int outPower, boolean isDirect) {

        if (outPower <= 0) {
            return false;
        }

        // Get the relevant blocks + materials
        GlowBlock blockUp   = block.getRelative(BlockFace.UP);
        GlowBlock blockMid  = block.getRelative(outDir.getModX(), outDir.getModY() + 0, outDir.getModZ());
        GlowBlock blockFwUp = block.getRelative(outDir.getModX(), outDir.getModY() + 1, outDir.getModZ());
        GlowBlock blockFwDn = block.getRelative(outDir.getModX(), outDir.getModY() - 1, outDir.getModZ());
        GlowBlock blockDn   = block.getRelative(BlockFace.DOWN);

        Material matUp   = (blockUp   != null ? blockUp  .getType() : null);
        Material matMid  = (blockMid  != null ? blockMid .getType() : null);
        Material matFwUp = (blockFwUp != null ? blockFwUp.getType() : null);
        Material matFwDn = (blockFwDn != null ? blockFwDn.getType() : null);
        Material matDn   = (blockDn   != null ? blockDn  .getType() : null);

        // Get some flags
        boolean continueMid = (matMid == Material.REDSTONE_WIRE || matMid == Material.DIODE_BLOCK_OFF || matMid == Material.DIODE_BLOCK_ON || matMid == Material.REDSTONE_COMPARATOR_OFF);
        boolean continueFwUp = (matFwUp == Material.REDSTONE_WIRE);
        boolean continueFwDn = (matFwDn == Material.REDSTONE_WIRE);

        boolean solidUp   = (matUp   != null && matUp  .isOccluding());
        boolean solidMid  = (matMid  != null && matMid .isOccluding());
        boolean solidFwUp = (matFwUp != null && matFwUp.isOccluding());
        boolean solidFwDn = (matFwDn != null && matFwDn.isOccluding());

        // Check if glowstone 
        boolean glowUp    = (matUp   == Material.GLOWSTONE);
        boolean glowDn    = (matDn   == Material.GLOWSTONE);

        //Check if on a slab
        boolean onSlab = (blockDn != null ? (blockDn.getState().getData() instanceof Step || blockDn.getState().getData() instanceof Stairs) : false);
        boolean slabMid = (blockMid != null) && ((blockMid.getState().getData() instanceof Step) || (blockMid.getState().getData() instanceof Stairs));

        if (forbidDir == outDir) {
            if (!slabMid) { //May go backwards if there's a slab
                return false;
            }
        }
        if (glowUp) {
            solidFwUp = false;
        }
        if (glowDn) {
            continueFwDn = false;
        }

        // Determine which one we use
        GlowBlock useBlock = null;
        if (continueFwDn && !solidMid && !onSlab) { //Redstone doesn't go downwards from slabs or from stairs
            // Downwards
            useBlock = blockFwDn;
            rsManager.traceFromBlockToBlock(block, useBlock, outDir, outPower, isDirect);           
        }
        if (continueFwUp && !solidUp) {
            // Upwards
            useBlock = blockFwUp;
            rsManager.traceFromBlockToBlock(block, useBlock, outDir, outPower, isDirect);
            if (forbidDir == outDir && slabMid) {
                useBlock = null; //Use block is used to detect if we have to inject power, and we need to do this if we were only going back
            }
            if (glowUp) {
                // Is there a wire 2 steps above us?
                GlowBlock blockUp2 = blockUp.getRelative(BlockFace.UP);
                Material matUp2 = (blockUp2 != null ? blockUp2.getType() : null);
                boolean wireUp2 = (matUp2 == Material.REDSTONE_WIRE);
                if (wireUp2) {
                    // Yes - trace from FwUp pointing in the direction of that wire.
                    traceBlockPowerRSWire(blockFwUp, rsManager, BlockFace.SELF, outDir.getOppositeFace(), outPower - 1, isDirect);
                }
            }
        }
        if (continueMid) {
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
        if (inPower <= rsManager.getNewBlockPower(block)) {
            return;
        }

        // Set power
        rsManager.setBlockPower(block, inPower);

        // Check if power sufficient
        if (inPower <= 1) {
            return;
        }

        // Spread out
        BlockFace forbidDir = flowDir.getOppositeFace();
        int outPower = inPower - 1;
        boolean nn = traceBlockPowerRSWire(block, rsManager, forbidDir, BlockFace.NORTH, outPower, false);
        boolean ns = traceBlockPowerRSWire(block, rsManager, forbidDir, BlockFace.SOUTH, outPower, false);
        boolean ne = traceBlockPowerRSWire(block, rsManager, forbidDir, BlockFace.EAST , outPower, false);
        boolean nw = traceBlockPowerRSWire(block, rsManager, forbidDir, BlockFace.WEST , outPower, false);

        // Sum the numbers
        int bsum = 0;
        if (nn) { bsum++; }
        if (ns) { bsum++; }
        if (ne) { bsum++; }
        if (nw) { bsum++; }

        // Flow behaves differently from top and bottom
        if (flowDir == BlockFace.UP || flowDir == BlockFace.DOWN) {
            if (bsum == 0) {
                // If there are no wires, "inject" in a spread
                traceBlockPowerInject(block, rsManager, BlockFace.NORTH, outPower);
                traceBlockPowerInject(block, rsManager, BlockFace.SOUTH, outPower);
                traceBlockPowerInject(block, rsManager, BlockFace.WEST, outPower);
                traceBlockPowerInject(block, rsManager, BlockFace.EAST, outPower);
            } else if (bsum == 1) {
                // If there is exactly one wire, "inject" in the OPPOSITE direction
                if (ns) traceBlockPowerInject(block, rsManager, BlockFace.NORTH, outPower);
                if (nn) traceBlockPowerInject(block, rsManager, BlockFace.SOUTH, outPower);
                if (ne) traceBlockPowerInject(block, rsManager, BlockFace.WEST , outPower);
                if (nw) traceBlockPowerInject(block, rsManager, BlockFace.EAST , outPower);
            }
        } else {
            // If there is exactly one wire (the one we came from!), "inject"
            if (bsum == 0) {
                traceBlockPowerInject(block, rsManager, flowDir, outPower);
            }
        }

        // Move to floor
        rsManager.traceFromBlock(block, BlockFace.DOWN, outPower, false);
    }

    @Override
    public void traceBlockPowerEnd(GlowBlock block, RSManager rsManager, int power) {
        // Set block charge
        assert (power >= 0 && power <= 15);
        block.setTypeIdAndData(getMaterial().getId(), (byte) (power & 15), false);
    }

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        GlowBlock floor = block.getRelative(BlockFace.DOWN);
        if (floor != null) {
            Material mat = floor.getType();
            if (mat.isOccluding()) {
                return true;
            }
            if (mat == Material.GLOWSTONE) {
                return true;
            }
            if (mat == Material.HOPPER || mat == Material.REDSTONE_BLOCK) {
                return true;
            }
            MaterialData md = floor.getState().getData();
            if (md instanceof Step) {
                if (((Step) md).isInverted()) { //You can place redstone on upper slabs
                    return true;
                }
            } else if (md instanceof Stairs) {
                if (((Stairs) md).isInverted()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
        block.getWorld().getRSManager().addFlush(block);
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.REDSTONE, 1, (short) 0)));
    }
} 
