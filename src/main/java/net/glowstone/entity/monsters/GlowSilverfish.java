package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Silverfish;

public class GlowSilverfish extends GlowMonster implements Silverfish {

    public GlowSilverfish(Location location) {
        super(location, EntityType.SILVERFISH);
    }
}
