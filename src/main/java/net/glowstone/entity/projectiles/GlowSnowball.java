package net.glowstone.entity.projectiles;

import net.glowstone.entity.GlowProjectile;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowball;
import org.bukkit.projectiles.ProjectileSource;

public class GlowSnowball extends GlowProjectile implements Snowball {

    public GlowSnowball(Location location) {
        this(location, null);
    }

    public GlowSnowball(Location location, ProjectileSource shooter) {
        super(location, EntityType.SNOWBALL, shooter);
    }
}
