package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import net.glowstone.block.GlowBlock;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class BlockDeadBush extends BlockPlant {

    private final Collection<ItemStack> emptyStack = Collections.unmodifiableList(Arrays.asList(new ItemStack[0]));

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        final Material type = block.getRelative(BlockFace.DOWN).getType();
        if (type.equals(Material.SAND) || type.equals(Material.DIRT)
                || type.equals(Material.HARD_CLAY) || type.equals(Material.STAINED_CLAY)) {
            return true;
        }
        return false;
    }

    @Override
    public final Collection<ItemStack> getDrops(GlowBlock block) {
        return emptyStack;
    }
}
