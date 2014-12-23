package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockSugarCane extends DefaultBlockType {
    private static final BlockFace[] DIRECT_FACES = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH};

    public BlockSugarCane() {
        super(
                new BlockNeedsAttached(),
                new BlockDirectDrops(Material.SUGAR_CANE)
        );
    }

    @Override
    public void onNearBlockChanged(GlowBlock block, BlockFace face, GlowBlock changedBlock, Material oldType, byte oldData, Material newType, byte newData) {
        updatePhysics(block);
    }

    @Override
    public Boolean canPlaceAt(GlowBlock block, BlockFace against) {
        Block below = block.getRelative(BlockFace.DOWN);
        Material type = below.getType();
        switch (type) {
            case SUGAR_CANE_BLOCK:
                return true;
            case DIRT:
            case GRASS:
            case SAND:
                return isNearWater(below);
        }
        return false;
    }


    private boolean isNearWater(Block block) {
        for (BlockFace face : DIRECT_FACES) {
            switch (block.getRelative(face).getType()) {
                case WATER:
                case STATIONARY_WATER:
                    return true;
            }
        }

        return false;
    }
}
