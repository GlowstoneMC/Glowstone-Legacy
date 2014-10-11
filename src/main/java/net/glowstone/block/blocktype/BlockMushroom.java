package net.glowstone.block.blocktype;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.world.StructureGrowEvent;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.generator.HugeMushroomGenerator;
import net.glowstone.util.BlockStateDelegate;

public class BlockMushroom extends BlockType implements IBlockGrowable {
    private final Material mushroomType;
    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

    public BlockMushroom(Material mushroomType) {
        this.mushroomType = mushroomType;
    }

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        final GlowBlock belowBlock = block.getRelative(BlockFace.DOWN);
        final Material type = belowBlock.getType();
        if (type.equals(Material.GRASS)
                || (type.equals(Material.DIRT) && belowBlock.getData() != 2)
                && block.getLightLevel() < 13) { // checking light level for dirt, coarse dirt and grass
            return true;
        } else if (type.equals(Material.MYCEL)
                || (type.equals(Material.DIRT) && belowBlock.getData() == 2)) {
            // not checking light level if mycel or podzol
            return true;
        }
        return false;
    }

    @Override
    public boolean isFertilizable(GlowBlock block) {
        return true;
    }

    @Override
    public boolean canGrowWithChance(GlowBlock block) {
        return (double) random.nextFloat() < 0.4D;
    }

    @Override
    public void grow(GlowPlayer player, GlowBlock block) {
        TreeType type;
        if (mushroomType.equals(Material.BROWN_MUSHROOM)) {
            type = TreeType.BROWN_MUSHROOM;
        } else if (mushroomType.equals(Material.RED_MUSHROOM)) {
            type = TreeType.RED_MUSHROOM;
        } else {
            return;
        }
        final BlockStateDelegate delegate = new BlockStateDelegate();
        final HugeMushroomGenerator generator = new HugeMushroomGenerator(delegate);
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
    }
}
