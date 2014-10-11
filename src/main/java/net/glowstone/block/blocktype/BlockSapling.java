package net.glowstone.block.blocktype;

import java.util.List;
import java.util.Random;

import org.bukkit.TreeType;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Tree;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.generator.TreeGenerator;
import net.glowstone.util.BlockStateDelegate;

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
    public void grow(GlowPlayer player, GlowBlock block) {
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
            final BlockStateDelegate delegate = new BlockStateDelegate();
            final TreeGenerator generator = new TreeGenerator(delegate);
            if (generator.generate(random, block.getLocation(), type)) {
                final List<BlockState> blockStates = delegate.getBlockStates();
                StructureGrowEvent growEvent =
                        new StructureGrowEvent(block.getLocation(), type, true, player, blockStates);
                EventFactory.callEvent(growEvent);
                if (!growEvent.isCancelled()) {
                    for (BlockState state : blockStates) {
                        state.update(true);
                    }
                }
            }
        } else {
            warnMaterialData(Tree.class, data);
        }
    }
}
