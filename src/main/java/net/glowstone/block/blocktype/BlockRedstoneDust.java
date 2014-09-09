package net.glowstone.block.blocktype;

import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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
        // TODO!
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
