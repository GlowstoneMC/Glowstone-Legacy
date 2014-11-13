package net.glowstone.generator.objects;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.DirectionalContainer;

public class RandomItemsContent {

    private final Random random;
    private final Map<WeightedItem, Integer> content = new HashMap<WeightedItem, Integer>();

    public RandomItemsContent(Random random) {
        this.random = random;
    }

    public void addItem(WeightedItem item, int weight) {
        content.put(item, weight);
    }

    public boolean fillContainer(Location location, DirectionalContainer container, int maxStacks) {

        final Block block = location.getBlock();
        block.setType(container.getItemType());
        final BlockState state = block.getState();
        state.setData(container);
        state.update(true);
        if (state instanceof InventoryHolder) {
            Inventory inventory = ((InventoryHolder) state).getInventory();
            final int size = inventory.getSize();
            for (int i = 0; i < maxStacks; i++) {
                final WeightedItem item = getRandomItem();
                if (item != null) {
                    for (ItemStack stack: item.getItemStacks(random)) {
                        // slot can be overriden hence maxStacks can be less than what's expected
                        inventory.setItem(random.nextInt(size), stack);
                    }
                }
            }
        }

        return true;
    }

    public WeightedItem getRandomItem() {
        int totalWeight = 0;
        for (int i : content.values()) {
            totalWeight += i;
        }
        if (totalWeight <= 0) {
            return null;
        }
        int weight = random.nextInt(totalWeight);
        for (Entry<WeightedItem, Integer> entry : content.entrySet()) {
            weight -= entry.getValue();
            if (weight < 0) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static class WeightedItem {

        private final int maxAmount;
        private final ItemStack stack;

        public WeightedItem(Material type, int minAmount, int maxAmount) {
            this(type, 0, minAmount, maxAmount);
        }

        public WeightedItem(Material type, int data, int minAmount, int maxAmount) {
            stack = new ItemStack(type, minAmount, (short) data);
            this.maxAmount = maxAmount;
        }

        public Collection<ItemStack> getItemStacks(Random random) {
            int minAmount = stack.getAmount();
            int amount = random.nextInt(maxAmount - minAmount + 1) + minAmount;
            if (amount <= stack.getMaxStackSize()) {
                final ItemStack adjustedStack = stack.clone();
                adjustedStack.setAmount(amount);
                return Collections.unmodifiableList(Arrays.asList(adjustedStack));
            } else {
                final ItemStack[] stacks = new ItemStack[amount];
                for (int i = 0; i < amount; i++) {
                    stacks[i] = stack.clone();
                    stacks[i].setAmount(1);
                }
                return Collections.unmodifiableList(Arrays.asList(stacks));
            }
        }
    }
}
