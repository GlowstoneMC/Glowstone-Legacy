package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;

public class ItemFishingRod extends ItemTool {

    public ItemFishingRod() {
        super(Material.FISHING_ROD.getMaxDurability());
    }

    @Override
    public short calculateBreakDamage(GlowBlock target) {
        return 0;
    }
}
