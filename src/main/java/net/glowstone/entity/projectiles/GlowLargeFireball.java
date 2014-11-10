package net.glowstone.entity.projectiles;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LargeFireball;
import org.bukkit.projectiles.ProjectileSource;

public class GlowLargeFireball extends GlowFireball implements LargeFireball {

    public GlowLargeFireball(Location location) {
        this(location, null);
    }

    public GlowLargeFireball(Location location, ProjectileSource shooter) {
        super(location, EntityType.FIREBALL, shooter);
    }
}
