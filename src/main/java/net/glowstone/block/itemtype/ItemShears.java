package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;

import org.bukkit.Material;

public class ItemShears extends ItemTool {

    public ItemShears(Material material) {
        super(material.getMaxDurability());
    }

    @Override
    public int calculateBreakDamage(GlowBlock target) {
        return target.getType().equals(Material.LEAVES) ? 1 : 0;
    }
}
