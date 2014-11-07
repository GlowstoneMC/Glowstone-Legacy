package net.glowstone.generator.objects;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TreasureChest {

    private final Random random;
    private final Material type;
    private final HashMap<Treasure, Integer> content = new HashMap<Treasure, Integer>();

    public TreasureChest(Random random) {
        this(random, Material.CHEST);
    }

    public TreasureChest(Random random, Material type) {
        if (type != Material.CHEST && type != Material.TRAPPED_CHEST) {
            throw new IllegalArgumentException("Invalid chest material !");
        }
        this.random = random;
        this.type = type;
    }

    protected void addTreasure(Treasure treasure, int weight) {
        content.put(treasure, weight);
    }

    public boolean generate(Location location, BlockFace facing, int maxStacks) {

        final Block block = location.getBlock();
        block.setType(type);
        final BlockState state = block.getState();
        state.setData(new org.bukkit.material.Chest(facing));
        state.update(true);
        //if (state instanceof Chest) {
            Inventory inventory = ((Chest) state).getBlockInventory();
            final int size = inventory.getSize();
            for (int i = 0; i < maxStacks; i++) {
                final Treasure treasure = getRandomTreasure();
                if (treasure != null) {
                    for (ItemStack stack: treasure.getItemStacks(random)) {
                        // slot can be overriden hence maxStacks can be less than what's expected
                        inventory.setItem(random.nextInt(size), stack);
                    }
                }
            }
        //}

        return true;
    }

    public Treasure getRandomTreasure() {
        int totalWeight = 0;
        for (int i : content.values()) {
            totalWeight += i;
        }
        if (totalWeight <= 0) {
            return null;
        }
        int weight = random.nextInt(totalWeight);
        for (Entry<Treasure, Integer> entry : content.entrySet()) {
            weight -= entry.getValue();
            if (weight < 0) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static class Treasure {

        private final int maxAmount;
        private final ItemStack stack;

        public Treasure(Material type, int minAmount, int maxAmount) {
            this(type, 0, minAmount, maxAmount);
        }

        public Treasure(Material type, int data, int minAmount, int maxAmount) {
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
