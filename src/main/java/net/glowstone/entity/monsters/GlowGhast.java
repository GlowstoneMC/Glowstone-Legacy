package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowFlying;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;

public class GlowGhast extends GlowFlying implements Ghast {

    private int explosionPower;

    public GlowGhast(Location location) {
        super(location, EntityType.GHAST);
    }

    public void setExplosionPower(int explosionPower) {
        this.explosionPower = explosionPower;
    }

    public int getExplosionPower() {
        return explosionPower;
    }
}
