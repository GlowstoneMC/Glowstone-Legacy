package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;

import org.bukkit.Material;

public class ItemSword extends ItemTool {
    
    public ItemSword(Material material) {
        super(material.getMaxDurability());
    }
    
    @Override
    public int calculateBreakDamage(GlowBlock target) {
        return 2;
    }
}
