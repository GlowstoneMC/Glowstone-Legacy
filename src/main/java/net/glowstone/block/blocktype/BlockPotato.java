package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockPotato extends BlockType {
    public BlockPotato() {
        super.setDrops(new ItemStack(Material.POTATO_ITEM, 1));
    }
}
