package net.glowstone.inventory;

import org.bukkit.Material;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class GlowDoubleChestInventory extends GlowInventory implements DoubleChestInventory {
    private static final int SIZE = 54, SPLITTER = 27;

    private final GlowInventory second;
    private final GlowInventory first;
    private final boolean firstIsLeft;

    public GlowDoubleChestInventory(GlowInventory first, GlowInventory second, boolean firstIsLeft) {
        super(null, InventoryType.CHEST, SIZE);
        this.first = first;
        this.second = second;
        this.firstIsLeft = firstIsLeft;
        fill();
    }

    public GlowDoubleChestInventory(GlowInventory first, GlowInventory second, boolean firstIsLeft, String title) {
        super(null, InventoryType.CHEST, SIZE, title);
        this.first = first;
        this.second = second;
        this.firstIsLeft = firstIsLeft;
        fill();
    }

    private void fill() {
        super.replaceContent(0, first.getContents());
        super.replaceContent(SPLITTER, second.getContents());
    }

    private void update() {
        ItemStack[] content = getContents();
        for (int i = 0; i < content.length; i++) {
            if (i < SPLITTER) {
                first.setItem(i, content[i]);
            } else {
                second.setItem(i - SPLITTER, content[i]);
            }
        }
    }

    @Override
    public Inventory getLeftSide() {
        return firstIsLeft ? first : second;
    }

    @Override
    public Inventory getRightSide() {
        return firstIsLeft ? second : first;
    }

    @Override
    public DoubleChest getHolder() {
        return new DoubleChest(this);
    }

    /////////////////////////////////////////////////////////////////
    // Override super methods to track modifying of the inventory

    @Override
    public void replaceContent(int beginIndex, ItemStack... content) {
        super.replaceContent(beginIndex, content);
        update();
    }

    @Override
    public void setContents(ItemStack[] content) {
        super.setContents(content);
        update();
    }

    @Override
    public void addViewer(HumanEntity viewer) {
        super.addViewer(viewer);
        first.addViewer(viewer);
        second.addViewer(viewer);
    }

    @Override
    public void removeViewer(HumanEntity viewer) {
        super.removeViewer(viewer);
        first.removeViewer(viewer);
        second.removeViewer(viewer);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        first.setTitle(title);
        second.setTitle(title);
    }

    @Override
    public void setItem(int index, ItemStack item) {
        super.setItem(index, item);
        if (index < SPLITTER)
            first.setItem(index, item);
        else
            second.setItem(index - SPLITTER, item);
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer, ItemStack> result = super.addItem(items);
        update();
        return result;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        HashMap<Integer, ItemStack> result = super.removeItem(items);
        update();
        return result;
    }

    @Override
    public void remove(int materialId) {
        super.remove(materialId);
        update();
    }

    @Override
    public void remove(Material material) {
        super.remove(material);
        update();
    }

    @Override
    public void remove(ItemStack item) {
        super.remove(item);
    }

    @Override
    public void clear(int index) {
        super.clear(index);
        update();
    }

    @Override
    public void clear() {
        super.clear();
        update();
    }
}
