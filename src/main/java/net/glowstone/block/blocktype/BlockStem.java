package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import net.glowstone.block.GlowBlock;

public class BlockStem extends BlockPlant implements IBlockGrowable {
    private Material plantType;
    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

    public BlockStem(Material plantType) {
        this.plantType = plantType;
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
        // TODO
        // take care of ripe stage
        if (plantType.equals(Material.MELON_STEM)) {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.MELON_SEEDS, random.nextInt(4))));
        } else if (plantType.equals(Material.PUMPKIN_STEM)) {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.PUMPKIN_SEEDS, random.nextInt(4))));
        }
        return Collections.unmodifiableList(Arrays.asList(new ItemStack[0]));
    }

    @Override
    public void fertilize(GlowBlock block) {
        int state = block.getData()
                + (random.nextInt(CropState.MEDIUM.ordinal())
                + CropState.VERY_SMALL.ordinal());
        if (state > CropState.RIPE.ordinal()) {
            state = CropState.RIPE.ordinal();
        }
        block.setData((byte) state);
        // TODO
        // the method below should be called from tick
        // and not in the fertilize method
        ripe(block); // FIXME
    }

    private void ripe(GlowBlock block) {
        if (block.getData() == CropState.RIPE.ordinal()) {
            int n = random.nextInt(4);
            int x = block.getX();
            int z = block.getZ();
            switch (n) {
                case 1:
                    x++;
                    break;
                case 2:
                    z = block.getZ() - 1;
                    break;
                case 3:
                    z++;
                    break;
                default:
                    x = block.getX() - 1;
            }
            final GlowBlock targetBlock = block.getWorld().getBlockAt(x, block.getY(), z);
            final GlowBlock belowTargetBlock = targetBlock.getRelative(BlockFace.DOWN);
            if (targetBlock.getType().equals(Material.AIR)
                    && (belowTargetBlock.getType().equals(Material.SOIL)
                    || belowTargetBlock.getType().equals(Material.DIRT)
                    || belowTargetBlock.getType().equals(Material.GRASS))) {
                if (plantType.equals(Material.MELON_STEM)) {
                    targetBlock.setType(Material.MELON_BLOCK);
                } else if (plantType.equals(Material.PUMPKIN_STEM)) {
                    targetBlock.setType(Material.PUMPKIN);
                }
            }
        }
    }
}
