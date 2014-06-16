package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockQuartzOre extends BlockType {
    public BlockQuartzOre() {
        super.setDrops(new ItemStack(Material.QUARTZ, 1));
    }
}
