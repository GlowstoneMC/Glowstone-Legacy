package net.glowstone.block.blocktype;

import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class BlockTorch extends BlockType {
    private final Material matType;

    public BlockTorch(Material matType) {
        this.matType = matType;
    }

    private BlockFace getOwnFacing(GlowBlock block) {
        switch(block.getData()) {
            case 1:
                return BlockFace.EAST;
            case 2:
                return BlockFace.WEST;
            case 3:
                return BlockFace.SOUTH;
            case 4:
                return BlockFace.NORTH;
            default:
                return BlockFace.UP;
        }
    }

    private int getFacing(BlockFace face) {
        switch (face) {
            case EAST:
                return 1;
            case WEST:
                return 2;
            case SOUTH:
                return 3;
            case NORTH:
                return 4;
        }

        return 5;
    }

    @Override
    public int getBlockPower(GlowBlock block, BlockFace face, boolean isDirect) {
        if(block.getType() == Material.REDSTONE_TORCH_ON) {
            if(face != getOwnFacing(block).getOppositeFace()) {
                return 15;
            }
        }
        return 0;
    }

    @Override
    public boolean isRedSource(GlowBlock block) {
        Material thisMat = getMaterial();
        return thisMat == Material.REDSTONE_TORCH_ON || thisMat == Material.REDSTONE_TORCH_OFF;
    }

    @Override
    public void traceBlockPowerEnd(GlowBlock block, RSManager rsManager, int power) {
        // Handle power change.
        // TODO: make this actually work
        Material mat = block.getType();
        if(mat == Material.REDSTONE_TORCH_ON && power == 0) {
            mat = Material.REDSTONE_TORCH_OFF;
            block.setTypeIdAndData(mat.getId(), (byte)block.getTypeId(), false);
        } else if(mat == Material.REDSTONE_TORCH_OFF && power != 0) {
            mat = Material.REDSTONE_TORCH_ON;
            block.setTypeIdAndData(mat.getId(), (byte)block.getTypeId(), false);
        }

        rsManager.addSource(block);
    }

    /**
     * Trace a redstone pulse from an RS torch outwards.
     * NOTE: This function can be extended to cater for inPower and isDirect, need-permitting.
     * @param srcBlock The block we are flowing from.
     * @param rsManager The RSManager used for tracking.
     * @param blockDir The direction we cannot flow in due to it leading back to our source.
     * @param toDir The direction of redstone flow from this block.
     * @param isDirect Whether the flow is direct or not.
     */
    private void traceBlockPowerFromRSTorch(GlowBlock srcBlock, RSManager rsManager, BlockFace blockDir, BlockFace toDir, boolean isDirect) {
        // Get the blockDir check out of the way.
        if(blockDir == toDir) {
            return;
        }

        // Trace outwards.
        rsManager.traceFromBlock(srcBlock, toDir, 15, isDirect);
    }

    @Override
    public void traceBlockPowerInit(GlowBlock block, RSManager rsManager) {
        Material thisMat = getMaterial();
        if(thisMat == Material.REDSTONE_TORCH_ON) {
            rsManager.setBlockCharge(block, 15, false);
        } else if(thisMat == Material.REDSTONE_TORCH_OFF) {
            // Set the charge so it can be restored on the next tick
            rsManager.setBlockCharge(block, 15, false);
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
            rsManager.setBlockCharge(block, 0, false);
        } else {
            // Disregard this.
        }
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        state.setType(matType);
        state.setRawData((byte)getFacing(face));
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(matType, 1, (byte)0)));
    }
} 
