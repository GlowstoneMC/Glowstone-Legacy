package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockMycelium extends BlockType {
    public BlockMycelium() {
        super.setDrops(new ItemStack(Material.DIRT, 1));
    }
}
