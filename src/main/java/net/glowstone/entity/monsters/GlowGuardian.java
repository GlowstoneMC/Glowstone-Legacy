package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Guardian;

public class GlowGuardian extends GlowMonster implements Guardian {

    private boolean isElder = false;

    public GlowGuardian(Location location) {
        super(location, EntityType.GUARDIAN);
    }

    @Override
    public boolean isElder() {
        return this.isElder;
    }

    @Override
    public void setElder(boolean isElder) {
        this.isElder = isElder;
    }
}
