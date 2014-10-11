package net.glowstone.block.blocktype;

import java.util.List;
import java.util.Random;

import org.bukkit.TreeSpecies;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
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
    public boolean canTickRandomly() {
        return true;
    }

    @Override
    public void updateBlock(GlowBlock block) {
        if (block.getRelative(BlockFace.UP).getLightLevel() >= 9 && random.nextInt(7) == 0) {
            int dataValue = block.getData();
            if ((dataValue & 8) == 0) {
                block.setData((byte) (dataValue | 8));
            } else {
                final MaterialData data = block.getState().getData();
                if (data instanceof Tree) {
                    final Tree tree = (Tree) data;
                    final TreeType type = getTreeType(tree.getSpecies());
                    block.getWorld().generateTree(block.getLocation(), type);
                } else {
                    warnMaterialData(Tree.class, data);
                }
            }
        }
    }

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
        final MaterialData data = block.getState().getData();
        if (data instanceof Tree) {
            final Tree tree = (Tree) data;
            final TreeType type = getTreeType(tree.getSpecies());
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

    private TreeType getTreeType(TreeSpecies species) {
        switch (species) {
            case GENERIC:
                return TreeType.TREE;
            case REDWOOD:
                return TreeType.REDWOOD;
            case BIRCH:
                return TreeType.BIRCH;
            case JUNGLE:
                return TreeType.JUNGLE;
            case ACACIA:
                return TreeType.ACACIA;
            case DARK_OAK:
                return TreeType.DARK_OAK;
            default:
                return TreeType.TREE;
        }
    }
}
