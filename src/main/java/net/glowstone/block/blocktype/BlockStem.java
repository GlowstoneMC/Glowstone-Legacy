package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Pumpkin;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;

public class BlockStem extends BlockPlant implements IBlockGrowable {
    private Material plantType;
    private Material fruitType;
    private Material seedsType;
    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

    public BlockStem(Material plantType) {
        this.plantType = plantType;
        if (plantType.equals(Material.MELON_STEM)) {
            fruitType = Material.MELON_BLOCK;
            seedsType = Material.MELON_SEEDS;
        } else if (plantType.equals(Material.PUMPKIN_STEM)) {
            fruitType = Material.PUMPKIN;
            seedsType = Material.PUMPKIN_SEEDS;
        }
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
        if (block.getState().getRawData() >= CropState.RIPE.ordinal()) {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(seedsType, random.nextInt(4))));
        } else {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack[0]));
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
    public void fertilize(GlowBlock block) {
        final GlowBlockState state = block.getState();
        int cropState = block.getData()
            + (random.nextInt(CropState.MEDIUM.ordinal())
            + CropState.VERY_SMALL.ordinal());
        if (cropState > CropState.RIPE.ordinal()) {
            cropState = CropState.RIPE.ordinal();
        }
        state.setRawData((byte) cropState);
        // TODO
        // call onBlockGrow from EventFactory
        state.update(true);

        // TODO
        // the method below should be called from tick
        // and not in the fertilize method
        ripe(block); // FIXME
    }

    private void ripe(GlowBlock block) {
        int cropState = block.getData();
        if (cropState >= CropState.RIPE.ordinal()) {
            // check around there's not already a fruit
            if (block.getRelative(BlockFace.EAST).getType().equals(fruitType)
                    || block.getRelative(BlockFace.WEST).getType().equals(fruitType)
                    || block.getRelative(BlockFace.NORTH).getType().equals(fruitType)
                    || block.getRelative(BlockFace.SOUTH).getType().equals(fruitType)) {
                return;
            }
            // produce a fruit if possible
            int n = random.nextInt(4);
            BlockFace face;
            switch (n) {
                case 1:
                    face = BlockFace.WEST;
                    break;
                case 2:
                    face = BlockFace.NORTH;
                    break;
                case 3:
                    face = BlockFace.SOUTH;
                    break;
                default:
                    face = BlockFace.EAST;
            }
            final GlowBlock targetBlock = block.getRelative(face);
            final GlowBlockState targetBlockState = targetBlock.getState();
            final GlowBlock belowTargetBlock = targetBlock.getRelative(BlockFace.DOWN);
            if (targetBlock.getType().equals(Material.AIR)
                    && (belowTargetBlock.getType().equals(Material.SOIL)
                    || belowTargetBlock.getType().equals(Material.DIRT)
                    || belowTargetBlock.getType().equals(Material.GRASS))) {
                targetBlockState.setType(fruitType);
                if (fruitType.equals(Material.PUMPKIN)) {
                    targetBlockState.setData(new Pumpkin(face.getOppositeFace()));
                }
                targetBlockState.update(true);
            }
        } else {
            cropState++;
            final GlowBlockState state = block.getState();
            state.setRawData((byte) cropState);
            // TODO
            // call onBlockGrow from EventFactory
            state.update(true);
        }
    }
}
