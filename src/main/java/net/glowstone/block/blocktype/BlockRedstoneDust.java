package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BlockRedstoneDust extends BlockType {
    // TODO: handle properly

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.REDSTONE, 1, (short)0)));
    }
} 
