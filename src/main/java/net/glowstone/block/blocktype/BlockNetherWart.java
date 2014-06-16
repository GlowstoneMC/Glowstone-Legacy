package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BlockNetherWart extends BlockType {
    public BlockNetherWart() {
        super.setDrops(new ItemStack(Material.NETHER_STALK, 1));
    }
}
