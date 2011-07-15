package net.glowstone.block.properties;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class GravelProperties {

    private GravelProperties() {
    }
    
    public static ItemStack[] drops() {
        ItemStack[] ret = {new ItemStack(Material.GRAVEL, 1)};
        if (new Random().nextDouble() >= 0.9) {
                ret[0] = new ItemStack(Material.FLINT, 1);
            }
        return ret;
    }
}
