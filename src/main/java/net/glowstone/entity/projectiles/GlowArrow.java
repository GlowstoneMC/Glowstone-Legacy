package net.glowstone.entity.projectiles;

import net.glowstone.entity.GlowProjectile;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.projectiles.ProjectileSource;

public class GlowArrow extends GlowProjectile implements Arrow {

    private boolean critical;
    private int knockbackStrength;

    public GlowArrow(Location location) {
        super(location, EntityType.ARROW);
    }

    public GlowArrow(Location location, ProjectileSource shooter) {
        super(location, EntityType.ARROW, shooter);
    }

    @Override
    public int getKnockbackStrength() {
        return knockbackStrength;
    }

    @Override
    public void setKnockbackStrength(int knockbackStrength) {
        this.knockbackStrength = knockbackStrength;
    }

    @Override
    public boolean isCritical() {
        return critical;
    }

    @Override
    public void setCritical(boolean isCritical) {
        this.critical = isCritical;
    }
}
