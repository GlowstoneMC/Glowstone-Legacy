package net.glowstone.block.blocktype;

import java.util.Random;

import org.bukkit.TreeType;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;

import net.glowstone.block.GlowBlock;

public class BlockSapling extends BlockPlant implements IBlockGrowable {
    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

    @Override
    public boolean isFertilizable(GlowBlock block) {
        return true;
    }

    @Override
    public boolean canGrowWithChance(GlowBlock block) {
        return (double) random.nextFloat() < 0.45D;
    }

    @Override
    public void fertilize(GlowBlock block) {
        // TODO
        // make GlowWold.generateTree() to not overwrite blocks
        // and check there's enough place around to plant a tree
        // handle big trees
        final MaterialData data = block.getState().getData();
        if (data instanceof Tree) {
            final Tree tree = (Tree) data;
            TreeType type = null;
            switch (tree.getSpecies()) {
                case REDWOOD:
                    type = TreeType.REDWOOD;
                    break;
                case BIRCH:
                    type = TreeType.BIRCH;
                    break;
                case JUNGLE:
                    type = TreeType.JUNGLE;
                    break;
                case ACACIA:
                    type = TreeType.ACACIA;
                    break;
                case DARK_OAK:
                    type = TreeType.DARK_OAK;
                    break;
                default:
                    type = TreeType.TREE;
            }
            block.getWorld().generateTree(block.getLocation(), type);
        } else {
            warnMaterialData(Tree.class, data);
        }
    }
}
