package net.glowstone.block.blocktype;

import java.util.Random;

import org.bukkit.TreeType;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;

import net.glowstone.block.GlowBlock;

public class BlockSapling extends BlockType implements IBlockGrowable {

    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

    @Override
    public void fertilize(GlowBlock block) {
        // TODO
        // check there's enough place around the sapling
        // make generateTree to not overwrite blocks
        // handle big trees

        final MaterialData data = block.getState().getData();
        if (data instanceof Tree) {
            if (random.nextFloat() < 0.30F) {
                final Tree tree = (Tree) data;
                TreeType type = null;
                switch (tree.getSpecies()) {
                    case GENERIC:
                        type = TreeType.TREE;
                        break;
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
            }
        } else {
            warnMaterialData(Tree.class, data);
        }
    }
 }
