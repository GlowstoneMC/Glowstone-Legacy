package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class AbstractItemType implements ItemType {
    private ItemType base;

    @Override
    public final void onInit(ItemType base, int id) {
        this.base = base;
    }

    protected ItemType getBase() {
        return base;
    }

    @Override
    public Material getMaterial() {
        return base.getMaterial();
    }

    @Override
    public int getId() {
        return base.getId();
    }

    ////////////////////////////
    // Actions

    @Override
    public void rightClickAir(GlowPlayer player, ItemStack holding) {

    }

    @Override
    public void rightClickBlock(GlowPlayer player, GlowBlock target, BlockFace face, ItemStack holding, Vector clickedLoc) {

    }
}
