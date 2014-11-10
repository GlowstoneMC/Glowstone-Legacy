package net.glowstone.entity.passive;

import net.glowstone.entity.GlowGolem;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Snowman;

public class GlowSnowman extends GlowGolem implements Snowman {

    public GlowSnowman(Location location) {
        super(location, EntityType.SNOWMAN);
    }
}
