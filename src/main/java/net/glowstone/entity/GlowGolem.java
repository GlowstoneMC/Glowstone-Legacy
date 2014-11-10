package net.glowstone.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Golem;

public abstract class GlowGolem extends GlowCreature implements Golem {

    protected GlowGolem(Location location, EntityType type) {
        super(location, type);
    }
}
