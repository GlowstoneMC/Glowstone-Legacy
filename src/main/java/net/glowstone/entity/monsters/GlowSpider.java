package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;

public class GlowSpider extends GlowMonster implements Spider {

    public GlowSpider(Location location) {
        super(location, EntityType.SPIDER);
    }

    protected GlowSpider(Location location, EntityType type) {
        super(location, type);
    }
}
