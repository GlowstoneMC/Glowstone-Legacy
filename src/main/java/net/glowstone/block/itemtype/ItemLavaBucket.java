package net.glowstone.block.itemtype;

import org.bukkit.Material;

public class ItemLavaBucket extends ItemFilledBucket {

    public ItemLavaBucket () {
        super(Material.LAVA);
        this.setMaxStackSize(1);
    }

}
