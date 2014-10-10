package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.inventory.ItemStack;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;

public class BlockCrops extends BlockPlant implements IBlockGrowable {
    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

    @Override
    public boolean canTickRandomly() {
        return true;
    }

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        if (block.getRelative(BlockFace.DOWN).getType().equals(Material.SOIL)) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        if (block.getData() >= CropState.RIPE.ordinal()) {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.SEEDS, random.nextInt(4)),
                new ItemStack(Material.WHEAT, 1)));
        } else {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.SEEDS, 1)));
        }
    }

    @Override
    public void updateBlock(GlowBlock block) {
        final GlowBlockState state = block.getState();
        int cropState = block.getData();
        if (cropState < CropState.RIPE.ordinal()) {
            if (random.nextInt(3) == 0) {
               cropState++;
            }
        }
        if (cropState > CropState.RIPE.ordinal()) {
            cropState = CropState.RIPE.ordinal();
        }
        state.setRawData((byte) cropState);
        BlockGrowEvent growEvent = new BlockGrowEvent(block, block.getState());
        EventFactory.callEvent(growEvent);
        if (!growEvent.isCancelled()) {
            state.update(true);
        }
    }

    @Override
    public boolean isFertilizable(GlowBlock block) {
        return block.getData() != CropState.RIPE.ordinal();
    }

    @Override
    public boolean canGrowWithChance(GlowBlock block) {
        return true;
    }

    @Override
    public void grow(GlowBlock block) {
        final GlowBlockState state = block.getState();
        int cropState = block.getData()
            + (random.nextInt(CropState.MEDIUM.ordinal())
            + CropState.VERY_SMALL.ordinal());
        if (cropState > CropState.RIPE.ordinal()) {
            cropState = CropState.RIPE.ordinal();
        }
        state.setRawData((byte) cropState);
        BlockGrowEvent growEvent = new BlockGrowEvent(block, block.getState());
        EventFactory.callEvent(growEvent);
        if (!growEvent.isCancelled()) {
            state.update(true);
        }
    }
}
