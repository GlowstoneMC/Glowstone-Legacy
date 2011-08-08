package net.glowstone.spout;

import java.util.Collection;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import org.getspout.spoutapi.inventory.InventoryBuilder;

/**
 * Inventory builder for Spout integration.
 */
public class GlowInventoryBuilder implements InventoryBuilder {

    public Inventory construct(ItemStack[] items, String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Inventory construct(Collection<ItemStack> items, String name) {
        return construct(items.toArray(new ItemStack[items.size()]), name);
    }

    public Inventory construct(int size, String name) {
        return construct(new ItemStack[size], name);
    }
    
}
