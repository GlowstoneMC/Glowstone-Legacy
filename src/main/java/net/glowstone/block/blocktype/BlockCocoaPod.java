package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockCocoaPod extends BlockType {
    public BlockCocoaPod() {
        super.setDrops(new ItemStack(Material.INK_SACK, 1, (short) 3));
    }
}
