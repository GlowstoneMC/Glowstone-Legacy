package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockClay extends BlockType {
    public BlockClay() {
        super.setDrops(new ItemStack(Material.CLAY_BALL, 4));
    }
}
