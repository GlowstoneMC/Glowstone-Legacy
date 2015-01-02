package net.glowstone.inventory;

import net.glowstone.constants.ItemIds;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * A class which represents an inventory and the items it contains.
 */
public class GlowInventory implements Inventory {

    /**
     * The list of humans viewing this inventory.
     */
    private final Set<HumanEntity> viewers = new HashSet<>();

    /**
     * The owner of this inventory.
     */
    private final InventoryHolder owner;

    /**
     * The type of this inventory.
     */
    private final InventoryType type;

    /**
     * This inventory's contents.
     */
    private final ItemStack[] slots;

    /**
     * This inventory's slot types.
     */
    protected final SlotType[] slotTypes;

    /**
     * The inventory's name.
     */
    private String title;

    /**
     * The inventory's maximum stack size.
     */
    private int maxStackSize = 64;

    public GlowInventory(InventoryHolder owner, InventoryType type) {
        this(owner, type, type.getDefaultSize(), type.getDefaultTitle());
    }

    public GlowInventory(InventoryHolder owner, InventoryType type, int size) {
        this(owner, type, size, type.getDefaultTitle());
    }

    public GlowInventory(InventoryHolder owner, InventoryType type, int size, String title) {
        this.owner = owner;
        this.type = type;
        this.title = title;
        slots = new ItemStack[size];
        slotTypes = new SlotType[size];
        Arrays.fill(slotTypes, SlotType.CONTAINER);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Internals

    /**
     * Add a viewer to the inventory.
     * @param viewer The HumanEntity to add.
     */
    public void addViewer(HumanEntity viewer) {
        if (!viewers.contains(viewer)) {
            viewers.add(viewer);
        }
    }

    /**
     * Remove a viewer from the inventory.
     * @param viewer The HumanEntity to remove.
     */
    public void removeViewer(HumanEntity viewer) {
        if (viewers.contains(viewer)) {
            viewers.remove(viewer);
        }
    }

    /**
     * Get the type of the specified slot.
     * @param slot The slot number.
     * @return The SlotType of the slot.
     */
    public SlotType getSlotType(int slot) {
        if (slot < 0) return SlotType.OUTSIDE;
        return slotTypes[slot];
    }

    /**
     * Check whether it is allowed for a player to insert the given ItemStack
     * at the slot, regardless of the slot's current contents. Should return
     * false for crafting output slots or armor slots which cannot accept
     * the given item.
     * @param slot The slot number.
     * @param stack The stack to add.
     * @return Whether the stack can be added there.
     */
    public boolean itemPlaceAllowed(int slot, ItemStack stack) {
        return getSlotType(slot) != SlotType.RESULT;
    }

    /**
     * Check whether, in a shift-click operation, an item of the specified type
     * may be placed in the given slot.
     * @param slot The slot number.
     * @param stack The stack to add.
     * @return Whether the stack can be added there.
     */
    public boolean itemShiftClickAllowed(int slot, ItemStack stack) {
        return itemPlaceAllowed(slot, stack);
    }

    /**
     * Set the custom title of this inventory or reset it to the default.
     * @param title The new title, or null to reset.
     */
    public void setTitle(String title) {
        if (title == null) {
            this.title = type.getDefaultTitle();
        } else {
            this.title = title;
        }
    }

    /**
     * Gets the number of slots in this inventory according to the protocol.
     * Some inventories have 0 slots in the protocol, despite having slots.
     * @return The numbers of slots
     */
    public int getRawSlots() {
        return getSize();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Basic Stuff

    @Override
    public final int getSize() {
        return slots.length;
    }

    @Override
    public final InventoryType getType() {
        return type;
    }

    @Override
    public InventoryHolder getHolder() {
        return owner;
    }

    @Override
    public final String getName() {
        return title;
    }

    @Override
    public final String getTitle() {
        return title;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public void setMaxStackSize(int size) {
        this.maxStackSize = size;
    }

    @Override
    public List<HumanEntity> getViewers() {
        return new ArrayList<>(viewers);
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }

    @Override
    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0) {
            // negative indices go from back
            index += getSize() + 1;
        }
        return new InventoryIterator(this, index);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Get, Set, Add, Remove

    @Override
    public ItemStack getItem(int index) {
        return slots[index];
    }

    @Override
    public void setItem(int index, ItemStack item) {
        slots[index] = ItemIds.sanitize(item);
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer, ItemStack> result = new HashMap<>();

        for (int i = 0; i < items.length; ++i) {
            ItemStack item = ItemStack.sanitize(items[i]);

            //fail silently on invalid item
            if (item == null) continue;

            while (item.getAmount() > 0) {
                int available = firstAvailable(item);

                if (available == -1) {
                    result.put(i, item);
                    break;
                } else {
                    ItemStack curItem = getItem(available);

                    int curAmount = curItem == null ? 0 : curItem.getAmount();
                    int add;

                    if (curAmount + item.getAmount() >= item.getMaxStackSize()) {
                        add = item.getMaxStackSize() - curAmount;
                    } else {
                        add = item.getAmount();
                    }

                    ItemStack temp = new ItemStack(item.getType(), curAmount + add);
                    setItem(available, temp);
                    item.setAmount(item.getAmount() - add);
                }
            }
        }

        return result;
    }

    public int firstAvailable(ItemStack item) {
        ItemStack[] inventory = getContents();
        if (item == null) {
            return -1;
        }
        for (int i = 0; i < inventory.length; ++i) {
            ItemStack curItem = inventory[i];
            if (curItem == null || (curItem.isSimilar(item) && curItem.getAmount() < curItem.getMaxStackSize())) {
                return i;
            }
        }
        return firstEmpty();
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        HashMap<Integer, ItemStack> result = new HashMap<>();

        for (int i = 0; i < items.length; ++i) {
            int mat = items[i].getTypeId();
            int toRemove = items[i].getAmount();
            short damage = items[i].getDurability();

            for (int j = 0; j < getSize(); ++j) {
                // Look for stacks to remove from.
                if (slots[j] != null && slots[j].getTypeId() == mat && slots[j].getDurability() == damage) {
                    if (slots[j].getAmount() > toRemove) {
                        slots[j].setAmount(slots[j].getAmount() - toRemove);
                    } else {
                        toRemove -= slots[j].getAmount();
                        slots[j] = null;
                    }
                }
            }

            if (toRemove > 0) {
                // Couldn't remove them all.
                result.put(i, new ItemStack(mat, toRemove, damage));
            }
        }

        return result;
    }

    @Override
    public ItemStack[] getContents() {
        return slots;
    }

    @Override
    public void setContents(ItemStack[] items) {
        if (items.length != slots.length) {
            throw new IllegalArgumentException("Length of items must be " + slots.length);
        }
        for (int i = 0; i < slots.length; ++i) {
            slots[i] = ItemIds.sanitize(items[i]);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Contains

    @Override
    public boolean contains(int materialId) {
        return first(materialId) >= 0;
    }

    @Override
    public boolean contains(Material material) {
        return first(material) >= 0;
    }

    @Override
    public boolean contains(ItemStack item) {
        return first(item) >= 0;
    }

    @Override
    public boolean contains(int materialId, int amount) {
        HashMap<Integer, ? extends ItemStack> found = all(materialId);
        int total = 0;
        for (ItemStack stack : found.values()) {
            total += stack.getAmount();
        }
        return total >= amount;
    }

    @Override
    public boolean contains(Material material, int amount) {
        return contains(material.getId(), amount);
    }

    @Override
    public boolean contains(ItemStack item, int amount) {
        return contains(item.getTypeId(), amount);
    }

    @Override
    public boolean containsAtLeast(ItemStack item, int amount) {
        return false;  // todo
    }

    ////////////////////////////////////////////////////////////////////////////
    // Find all

    @Override
    public HashMap<Integer, ItemStack> all(int materialId) {
        HashMap<Integer, ItemStack> result = new HashMap<>();
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i].getTypeId() == materialId) {
                result.put(i, slots[i]);
            }
        }
        return result;
    }

    @Override
    public HashMap<Integer, ItemStack> all(Material material) {
        return all(material.getId());
    }

    @Override
    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> result = new HashMap<>();
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i] != null && slots[i].equals(item)) {
                result.put(i, slots[i]);
            }
        }
        return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Find first

    @Override
    public int first(int materialId) {
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i] != null && slots[i].getTypeId() == materialId) return i;
        }
        return -1;
    }

    @Override
    public int first(Material material) {
        return first(material.getId());
    }

    @Override
    public int first(ItemStack item) {
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i] != null && slots[i].equals(item)) return i;
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        for (int i = 0; i < slots.length; ++i) {
            if (slots[i] == null) return i;
        }
        return -1;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Remove

    @Override
    public void remove(int materialId) {
        HashMap<Integer, ? extends ItemStack> stacks = all(materialId);
        for (Integer slot : stacks.keySet()) {
            setItem(slot, null);
        }
    }

    @Override
    public void remove(Material material) {
        HashMap<Integer, ? extends ItemStack> stacks = all(material);
        for (Integer slot : stacks.keySet()) {
            setItem(slot, null);
        }
    }

    @Override
    public void remove(ItemStack item) {
        HashMap<Integer, ? extends ItemStack> stacks = all(item);
        for (Integer slot : stacks.keySet()) {
            setItem(slot, null);
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Clear

    @Override
    public void clear(int index) {
        setItem(index, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < slots.length; ++i) {
            clear(i);
        }
    }

}
