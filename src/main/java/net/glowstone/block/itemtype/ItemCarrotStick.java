package net.glowstone.block.itemtype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;

public class ItemCarrotStick extends ItemTool {

    public ItemCarrotStick() {
        super(Material.CARROT_STICK.getMaxDurability());
    }

    @Override
    public short calculateBreakDamage(GlowBlock target) {
        return 0;
    }
}
