package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import net.glowstone.block.GlowBlock;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockCarrot extends BlockCrops {
    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        // TODO
        // take care of ripe stage
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.CARROT_ITEM, random.nextInt(4))));
    }
}
