package net.glowstone.block.itemtype;

import org.bukkit.Material;

public class ItemWaterBucket extends ItemFilledBucket {

    public ItemWaterBucket () {
        super(Material.WATER);
        this.setMaxStackSize(1);
    }

}
