package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import net.glowstone.block.GlowBlock;

import org.bukkit.Material;
import org.bukkit.NetherWartsState;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class BlockNetherWart extends BlockPlant {

    private final Random random = new Random();

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        final Material type = block.getRelative(BlockFace.DOWN).getType();
        if (type.equals(Material.SOUL_SAND)) {
            return true;
        }
        return false;
    }

    @Override
    public final Collection<ItemStack> getDrops(GlowBlock block) {
        if (block.getData() >= NetherWartsState.RIPE.ordinal()) {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.NETHER_STALK, random.nextInt(3) + 2)));
        } else {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.NETHER_STALK, 1)));
        }
    }
}
