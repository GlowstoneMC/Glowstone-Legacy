package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class DefaultItemType implements ItemType {
    private final ItemType[] features;
    private int id;

    public DefaultItemType(ItemType... features) {
        this.features = features;
    }

    public final void onInit(ItemType base, int id) {
        this.id = id;
        for (ItemType feature : features) {
            feature.onInit(this, id);
        }
    }

    public final int getId() {
        return id;
    }

    public final Material getMaterial() {
        return Material.getMaterial(id);
    }

    protected ItemType[] getFeatures() {
        return features;
    }

    protected <T> T throwDouble(T oldValue, T value) {
        if (oldValue != null && value != null)
            throw new IllegalStateException("Mismatching features (" + getClass().getSimpleName() + "): more than one features modifying a result!");
        if (value != null)
            return value;
        else
            return oldValue;
    }

    ///////////////////
    // Actions
    @Override
    public void rightClickAir(GlowPlayer player, ItemStack holding) {
        for (ItemType feature : features)
            feature.rightClickAir(player, holding);
    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {
        for (ItemType feature : features)
            feature.rightClickBlock(player, target, face, holding, clickedLoc);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Utility stuff

    @Override
    public final String toString() {
        return getClass().getSimpleName() + "{" + getId() + " -> " + getMaterial() + "}";
    }
}
