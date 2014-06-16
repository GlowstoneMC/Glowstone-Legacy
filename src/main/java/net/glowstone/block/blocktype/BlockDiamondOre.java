package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockDiamondOre extends BlockType {
    public BlockDiamondOre() {
        super.setDrops(new ItemStack(Material.DIAMOND, 1));
    }
}
