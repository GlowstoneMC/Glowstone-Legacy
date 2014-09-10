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

    private void traceBlockPowerRSWire(GlowBlock block, RSManager rsManager, BlockFace blockDir, BlockFace outDir, int inPower, boolean isDirect) {
        // Bail out early if we're flowing backwards
        if(outDir == blockDir) {
            return;
        }

        // Get the relevant blocks + materials
        GlowBlock blockOn   = block.getRelative(BlockFace.UP);
        GlowBlock blockMid  = block.getRelative(outDir.getModX(), outDir.getModY() + 0, outDir.getModZ());
        GlowBlock blockUp   = block.getRelative(outDir.getModX(), outDir.getModY() + 1, outDir.getModZ());
        GlowBlock blockDown = block.getRelative(outDir.getModX(), outDir.getModY() - 1, outDir.getModZ());

        // Get some flags
        boolean wireMid  = (blockMid  != null && blockMid .getType() == Material.REDSTONE_WIRE);
        boolean wireUp   = (blockUp   != null && blockUp  .getType() == Material.REDSTONE_WIRE);
        boolean wireDown = (blockDown != null && blockDown.getType() == Material.REDSTONE_WIRE);

        boolean solidOn   = (blockOn   != null && blockOn  .getType().isSolid());
        boolean solidMid  = (blockMid  != null && blockMid .getType().isSolid());
        boolean solidUp   = (blockUp   != null && blockUp  .getType().isSolid());
        boolean solidDown = (blockDown != null && blockDown.getType().isSolid());

        // Determine which one we use
        if(wireDown && !solidMid) {
            // Down
            // (Mid is nonsolid so Up cannot be RS dust)
            rsManager.traceFromBlockToBlock(block, blockDown, outDir, inPower, isDirect);
        } else if(solidMid && wireUp && !solidOn) {
            // Up
            rsManager.traceFromBlockToBlock(block, blockUp  , outDir, inPower, isDirect);
        } else if(wireMid) {
            // Mid
            rsManager.traceFromBlockToBlock(block, blockMid , outDir, inPower, isDirect);
        }
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
        traceBlockPowerRSWire(block, rsManager, blockDir, BlockFace.NORTH, outPower, false);
        traceBlockPowerRSWire(block, rsManager, blockDir, BlockFace.SOUTH, outPower, false);
        traceBlockPowerRSWire(block, rsManager, blockDir, BlockFace.EAST , outPower, false);
        traceBlockPowerRSWire(block, rsManager, blockDir, BlockFace.WEST , outPower, false);

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
        return floor != null && floor.getType().isSolid();
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.REDSTONE, 1, (short)0)));
    }
} 
