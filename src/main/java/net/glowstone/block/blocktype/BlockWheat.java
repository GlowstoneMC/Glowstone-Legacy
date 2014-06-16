package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockWheat extends BlockType {
    public BlockWheat() {
        super.setDrops(new ItemStack(Material.SEEDS, 1));
    }
}
