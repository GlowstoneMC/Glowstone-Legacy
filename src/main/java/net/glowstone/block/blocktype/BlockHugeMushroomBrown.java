package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class BlockHugeMushroomBrown extends BlockType {
    private final Random random = new Random();

    /**
     * Get the items that will be dropped by digging the block.
     *
     * @param block The block being dug.
     * @return The drops that should be returned.
     */
    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        int rnd = random.nextInt(100);
        if (rnd < 80) {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack[0]));
        } else {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.BROWN_MUSHROOM, rnd > 90 ? 2 : 1)));
        }
    }
}
