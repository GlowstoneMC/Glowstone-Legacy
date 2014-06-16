package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockCoalOre extends BlockType {
    public BlockCoalOre() {
        super.setDrops(new ItemStack(Material.COAL, 1));
    }
}
