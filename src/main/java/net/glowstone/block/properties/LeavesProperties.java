package net.glowstone.block.properties;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LeavesProperties {

    private LeavesProperties() {
    }
    
    public static ItemStack[] drops() {
        ItemStack[] ret = {null};
        if (new Random().nextDouble() > 0.95)
                ret[0] = new ItemStack(Material.SAPLING, 1);
        return ret;
    }
}
