package net.glowstone.block.itemtype;

import java.util.Arrays;
import net.glowstone.block.GlowBlock;
import org.bukkit.Material;

public class ItemShears extends ItemTool {

    public ItemShears() {
        super(Material.SHEARS.getMaxDurability());
    }

    @Override
    public short calculateBreakDamage(GlowBlock target) {
        return (short) (Arrays.asList(new Material[]{Material.LEAVES, Material.LONG_GRASS, Material.WOOL}).contains(target.getType()) ? 1 : 0);
    }
}
