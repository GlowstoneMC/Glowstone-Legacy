package net.glowstone.entity.projectiles;

import net.glowstone.entity.GlowProjectile;
import org.bukkit.Location;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.projectiles.ProjectileSource;

public class GlowEgg extends GlowProjectile implements Egg {

    public GlowEgg(Location location) {
        super(location, EntityType.EGG);
    }

    public GlowEgg(Location location, ProjectileSource shooter) {
        super(location, EntityType.EGG, shooter);
    }
}
