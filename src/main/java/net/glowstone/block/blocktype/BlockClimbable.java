package net.glowstone.block.blocktype;

import net.glowstone.GlowWorld;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import org.bukkit.block.BlockFace;

public abstract class BlockClimbable extends BlockType {
    
    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        GlowWorld world = block.getWorld();
        return  world.isBlockSideSolid(block.getRelative(BlockFace.NORTH), BlockFace.SOUTH) ||
                world.isBlockSideSolid(block.getRelative(BlockFace.SOUTH), BlockFace.NORTH) ||
                world.isBlockSideSolid(block.getRelative(BlockFace.EAST) , BlockFace.WEST)  ||
                world.isBlockSideSolid(block.getRelative(BlockFace.WEST) , BlockFace.EAST);
    }
}
