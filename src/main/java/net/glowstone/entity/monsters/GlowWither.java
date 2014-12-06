package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wither;

public class GlowWither extends GlowMonster implements Wither {

    public GlowWither(Location location) {
        super(location, EntityType.WITHER);
    }
}
