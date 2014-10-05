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
    }
}
