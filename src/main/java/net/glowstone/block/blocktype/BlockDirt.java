package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockDirt extends BlockType {
    public BlockDirt() {
        super.setDrops(new ItemStack(Material.DIRT, 1));
    }
}
