package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockBookshelf extends BlockType {
    public BlockBookshelf() {
        super.setDrops(new ItemStack(Material.BOOK, 3));
    }
}
