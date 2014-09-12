package net.glowstone.entity;

import org.bukkit.Location;
import org.bukkit.entity.Explosive;

public abstract class GlowExplosive extends GlowEntity implements Explosive {
    private float yield;
    private boolean isIncendiary;

    public GlowExplosive(Location location, float yield) {
        super(location);
        this.yield = yield;
        this.isIncendiary = false;
    }

    @Override
    public void setYield(float yield) {
        this.yield = yield;
    }

    @Override
    public float getYield() {
        return yield;
    }

    @Override
    public void setIsIncendiary(boolean isIncendiary) {
        this.isIncendiary = isIncendiary;
    }

    @Override
    public boolean isIncendiary() {
        return this.isIncendiary;
    }
}
