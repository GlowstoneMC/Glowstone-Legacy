package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class BlockCactus extends BlockType {
    private static final BlockFace[] nearBlocks = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST};

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        Material below = block.getRelative(BlockFace.DOWN).getType();
        if (below == Material.CACTUS || below == Material.SAND)
            return !hasNearBlocks(block);
        return false;
    }

    @Override
    public void onNearBlockChanged(GlowBlock block, BlockFace position, GlowBlock changedBlock, Material oldType, byte oldData, Material newType, byte newData) {
        updatePhysics(block);
    }

    @Override
    public void updatePhysics(GlowBlock me) {
        if (!canPlaceAt(me, BlockFace.DOWN))
            me.breakNaturally();
    }

    private boolean hasNearBlocks(GlowBlock block) {
        for (BlockFace face : nearBlocks)
            if (!canPlaceNear(block.getRelative(face).getType()))
                return true;

        return false;
    }

    private boolean canPlaceNear(Material type) {
        //TODO: return true for non-buildable blocks

        switch (type) {
            case AIR:
            case WATER:
            case STATIONARY_WATER:
            case LAVA:
            case STATIONARY_LAVA:
            case PORTAL:
            case ENDER_PORTAL:
            case RED_ROSE:
            case YELLOW_FLOWER:

                return true;
        }

        return false;
    }
}
