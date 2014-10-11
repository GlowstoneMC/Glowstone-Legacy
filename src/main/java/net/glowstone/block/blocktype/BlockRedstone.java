package net.glowstone.block.blocktype;

import net.glowstone.RSManager;
import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class BlockRedstone extends BlockType {
    @Override
    public boolean canBlockEmitPower(GlowBlock block, BlockFace face, boolean isDirect) {
        return !isDirect; //Does only emit indirect power
    }
    
    @Override
    public boolean isRedSource(GlowBlock block) {
        return true;
    }
    
    @Override
    public void traceBlockPowerInit(GlowBlock block, RSManager rsManager) {
        rsManager.setBlockPower(block, 15);
    }
    
    @Override
    public void traceBlockPowerStart(GlowBlock block, RSManager rsManager) {
        Material thisMat = getMaterial();
        if(thisMat != Material.REDSTONE_BLOCK) {
            return;
        }

        // Trace in all directions.
        rsManager.traceFromBlock(block, BlockFace.UP, 15, false);
        rsManager.traceFromBlock(block, BlockFace.DOWN, 15, false);
        rsManager.traceFromBlock(block, BlockFace.NORTH, 15, false);
        rsManager.traceFromBlock(block, BlockFace.SOUTH, 15, false);
        rsManager.traceFromBlock(block, BlockFace.WEST, 15, false);
        rsManager.traceFromBlock(block, BlockFace.EAST, 15, false);
    }
    
    @Override
    public void traceBlockPowerEnd(GlowBlock block, RSManager rsManager, int power) {
        rsManager.addSource(block);
    }
}
