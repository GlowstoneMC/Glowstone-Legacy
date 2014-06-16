package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockCobweb extends BlockType {
    public BlockCobweb() {
        super.setDrops(new ItemStack(Material.STRING, 1));
    }
}
