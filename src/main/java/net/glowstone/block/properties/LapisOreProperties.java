package net.glowstone.block.properties;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LapisOreProperties {
    
    private LapisOreProperties() {
    }
    
    public static ItemStack[] drops() {
        ItemStack[] ret = {new ItemStack(Material.INK_SACK, new Random().nextInt(5)+4, (short)4)};
        return ret;
    }
}
