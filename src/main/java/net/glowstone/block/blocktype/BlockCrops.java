package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import net.glowstone.block.GlowBlock;

public class BlockCrops extends BlockPlant implements IBlockGrowable {
    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

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
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.SEEDS, random.nextInt(4))));
    }

    @Override
    public void fertilize(GlowBlock block) {
        // TODO
    }
}
