package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockEmeraldOre extends BlockType {
    public BlockEmeraldOre() {
        super.setDrops(new ItemStack(Material.EMERALD, 1));
    }
}
