package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockFarmland extends BlockType {
    public BlockFarmland() {
        super.setDrops(new ItemStack(Material.DIRT, 1));
    }
}
