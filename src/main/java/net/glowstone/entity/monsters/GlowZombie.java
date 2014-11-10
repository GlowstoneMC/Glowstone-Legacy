package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class GlowZombie extends GlowMonster implements Zombie {

    private boolean isBaby = false;

    private boolean isVillager = false;

    private boolean canBreakDoors = true;

    private int conversionTime;

    public GlowZombie(Location location) {
        super(location, EntityType.ZOMBIE);
    }

    GlowZombie(Location location, EntityType type) {
        super(location, type);
    }

    @Override
    public boolean isBaby() {
        return isBaby;
    }

    @Override
    public void setBaby(boolean isBaby) {
        this.isBaby = isBaby;
    }

    @Override
    public boolean isVillager() {
        return isVillager;
    }

    @Override
    public void setVillager(boolean isVillager) {
        this.isVillager = isVillager;
    }

    public int getConversionTime() {
        return conversionTime;
    }

    public void setConversionTime(int time) {
        this.conversionTime = time;
    }

    public boolean canBreakDoors() {
        return canBreakDoors;
    }

    public void setCanBreakDoors(boolean canBreakDoors) {
        this.canBreakDoors = canBreakDoors;
    }
}
