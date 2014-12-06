package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;
import org.bukkit.Location;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;

public class GlowCreeper extends GlowMonster implements Creeper {

    private boolean isPowered = false;
    private short fuse;
    private int explosionRadius;
    private boolean ignited;

    public GlowCreeper(Location location) {
        super(location, EntityType.CREEPER);
    }

    @Override
    public boolean isPowered() {
        return isPowered;
    }

    @Override
    public void setPowered(boolean isPowered) {
        this.isPowered = isPowered;
    }

    public short getFuse() {
        return fuse;
    }

    public void setFuse(short fuse) {
        this.fuse = fuse;
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(int explosionRadius) {
        this.explosionRadius = explosionRadius;
    }

    public boolean isIgnited() {
        return ignited;
    }

    public void setIgnited(boolean isIgnited) {
        this.ignited = isIgnited;
    }
}
