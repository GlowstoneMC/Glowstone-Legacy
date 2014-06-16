package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockCarrot extends BlockType {
    public BlockCarrot() {
        super.setDrops(new ItemStack(Material.CARROT_ITEM, 1));
    }
}
