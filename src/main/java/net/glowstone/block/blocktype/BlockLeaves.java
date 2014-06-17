package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class BlockLeaves extends BlockType {
    private final Random random = new Random();

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        //todo: add logic to drop correct sapling type and apples
        if (random.nextInt(100) < 5) {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.SAPLING, 1)));
        }
        return Collections.unmodifiableList(Arrays.asList(new ItemStack[0]));
    }
}
