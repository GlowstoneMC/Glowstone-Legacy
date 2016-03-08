package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowLivingEntity;
import org.bukkit.Material;

public class ItemSword extends ItemTool {

    public ItemSword(Material material) {
        super(material.getMaxDurability());
    }

    @Override
    public short calculateBreakDamage(GlowBlock target) {
        return 2;
    }
    
    @Override
    protected short calculateAttackDamage(GlowLivingEntity target) {
        return 1;
    }
}
