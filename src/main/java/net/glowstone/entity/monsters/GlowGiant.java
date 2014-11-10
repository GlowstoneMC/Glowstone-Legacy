package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;

public class GlowGiant extends GlowMonster implements Giant {

    public GlowGiant(Location location) {
        super(location, EntityType.GIANT);
    }
}
