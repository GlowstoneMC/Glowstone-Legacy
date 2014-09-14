package net.glowstone.block.blocktype;

import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import java.util.*;

public class BlockRedstoneTorch extends BlockTorch {

    public BlockRedstoneTorch(Material matType) {
        super(matType, Material.REDSTONE_TORCH_OFF);
    }

    @Override
    public boolean canBlockEmitPower(GlowBlock block, BlockFace face, boolean isDirect) {
        // Torches do not emit behind themselves
        if (face == getOwnFacing(block).getOppositeFace()) {
            return false;
        }

        // Direct emissions ONLY go up
        if (face != BlockFace.UP && !isDirect) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isRedSource(GlowBlock block) {
        return true;
    }

    @Override
    public void traceBlockPowerEnd(GlowBlock block, RSManager rsManager, int power) {
        // Handle power change.
        Material mat = getMaterial();
        if(mat == Material.REDSTONE_TORCH_ON && power == 0) {
            mat = Material.REDSTONE_TORCH_OFF;
            block.setTypeIdAndData(mat.getId(), (byte)block.getData(), false);
        } else if(mat == Material.REDSTONE_TORCH_OFF && power != 0) {
            mat = Material.REDSTONE_TORCH_ON;
            block.setTypeIdAndData(mat.getId(), (byte)block.getData(), false);
        }

        rsManager.addSource(block);
    }

    /**
     * Trace a redstone pulse from an RS torch outwards.
     * NOTE: This function can be extended to cater for inPower and isDirect, need-permitting.
     * @param srcBlock The block we are flowing from.
     * @param rsManager The RSManager used for tracking.
     * @param forbidDir The direction we cannot flow in due to it leading back to our source.
     * @param toDir The direction of redstone flow from this block.
     * @param isDirect Whether the flow is direct or not.
     */
    private void traceBlockPowerFromRSTorch(GlowBlock srcBlock, RSManager rsManager, BlockFace forbidDir, BlockFace toDir, boolean isDirect) {
        // Get the forbidDir check out of the way.
        if(forbidDir == toDir) {
            return;
        }

        // Trace outwards.
        rsManager.traceFromBlock(srcBlock, toDir, 15, isDirect);
    }

    @Override
    public void traceBlockPowerInit(GlowBlock block, RSManager rsManager) {
        Material thisMat = getMaterial();
        if(thisMat == Material.REDSTONE_TORCH_ON) {
            // Set the charge to ensure that this is handled next tick.
            rsManager.setBlockPower(block, 15, false);
        } else if(thisMat == Material.REDSTONE_TORCH_OFF) {
            // Set the charge now.
            // If it needs to stay discharged, it will be discharged by other blocks.
            rsManager.setBlockPower(block, 15, false);
        }
    }

    @Override
    public void traceBlockPowerStart(GlowBlock block, RSManager rsManager) {
        // Abandon ship if this is not a charged RS torch.
        Material thisMat = getMaterial();
        if(thisMat != Material.REDSTONE_TORCH_ON) {
            return;
        }

        // Trace in all directions except self.
        BlockFace selfDir = getOwnFacing(block).getOppositeFace();
        traceBlockPowerFromRSTorch(block, rsManager, selfDir, BlockFace.UP, true);
        traceBlockPowerFromRSTorch(block, rsManager, selfDir, BlockFace.DOWN, false);
        traceBlockPowerFromRSTorch(block, rsManager, selfDir, BlockFace.NORTH, false);
        traceBlockPowerFromRSTorch(block, rsManager, selfDir, BlockFace.SOUTH, false);
        traceBlockPowerFromRSTorch(block, rsManager, selfDir, BlockFace.WEST, false);
        traceBlockPowerFromRSTorch(block, rsManager, selfDir, BlockFace.EAST, false);
    }

    @Override
    public void traceBlockPower(GlowBlock block, RSManager rsManager, Material srcMat, BlockFace flowDir, int inPower, boolean isDirect) {
        // Abandon ship if this is just a regular torch.
        Material thisMat = getMaterial();
        if(thisMat != Material.REDSTONE_TORCH_ON && thisMat != Material.REDSTONE_TORCH_OFF) {
            return;
        }
        // Determine if we are getting this from a source.
        boolean isSource = (getOwnFacing(block) == flowDir);
        if(isSource) {
            // Discharge this torch.
            rsManager.setBlockPower(block, 0, false);
        } else {
            // Disregard this.
        }
    }
} 
