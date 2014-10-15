package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockSoil extends BlockType {

    public BlockSoil() {
        setDrops(new ItemStack(Material.DIRT, 1));
    }
}
