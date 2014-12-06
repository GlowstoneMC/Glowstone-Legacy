package net.glowstone.entity.projectiles;

import net.glowstone.entity.GlowProjectile;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;

public abstract class GlowFireball extends GlowProjectile implements Fireball {

    private boolean isIncendiary;
    private float yield;

    public GlowFireball(Location location, EntityType type, ProjectileSource shooter) {
        super(location, type, shooter);
    }

    @Override
    public Vector getDirection() {
        return null;
    }

    @Override
    public void setDirection(Vector vector) {

    }

    @Override
    public float getYield() {
        return yield;
    }

    @Override
    public void setYield(float v) {
        yield = v;
    }

    @Override
    public void setIsIncendiary(boolean isIncendiary) {
        this.isIncendiary = isIncendiary;
    }

    @Override
    public boolean isIncendiary() {
        return isIncendiary;
    }
}
