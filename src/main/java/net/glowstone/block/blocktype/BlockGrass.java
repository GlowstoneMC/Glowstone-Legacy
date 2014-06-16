package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockGrass extends BlockType {
    public BlockGrass() {
        super.setDrops(new ItemStack(Material.DIRT, 1));
    }
}
