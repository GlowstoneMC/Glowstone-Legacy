package net.glowstone.entity.passive;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MushroomCow;


public class GlowMushroomCow extends GlowCow implements MushroomCow {

    public GlowMushroomCow(Location location) {
        super(location, EntityType.MUSHROOM_COW);
        setSize(0.9F, 1.3F);
    }
}
