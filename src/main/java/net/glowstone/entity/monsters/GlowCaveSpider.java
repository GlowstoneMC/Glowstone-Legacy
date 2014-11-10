package net.glowstone.entity.monsters;

import org.bukkit.Location;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.EntityType;

public class GlowCaveSpider extends GlowSpider implements CaveSpider {

    public GlowCaveSpider(Location location) {
        super(location, EntityType.CAVE_SPIDER);
    }
}
