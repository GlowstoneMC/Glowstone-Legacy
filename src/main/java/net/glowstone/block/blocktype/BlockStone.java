package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockStone extends BlockType {
    public BlockStone() {
        super.setDrops(new ItemStack(Material.COBBLESTONE, 1));
    }
}
