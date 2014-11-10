package net.glowstone.entity.passive;

import net.glowstone.entity.GlowCreature;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Squid;

public class GlowSquid extends GlowCreature implements Squid {

    public GlowSquid(Location location) {
        super(location, EntityType.SQUID);
    }
}
