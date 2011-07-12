package net.glowstone.block.properties;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class RedstoneOreProperties {
    
    private RedstoneOreProperties() {}
    
    public static ItemStack[] drops() {
        ItemStack[] ret = {new ItemStack(Material.REDSTONE, new Random().nextInt(2)+4)};
        return ret;
    }
}
