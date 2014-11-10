package net.glowstone.entity.monsters;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;

public class GlowMagmaCube extends GlowSlime implements MagmaCube {

    public GlowMagmaCube(Location location) {
        super(location, EntityType.MAGMA_CUBE);
    }
}
