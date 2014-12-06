package net.glowstone.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.NPC;

/**
 * Represents an Animal, such as a Cow
 */
public class GlowNPC extends GlowCreature implements NPC {

    /**
     * Creates a new ageable animal.
     * @param location The location of the animal.
     * @param type The type of animal.
     */
    public GlowNPC(Location location, EntityType type) {
        super(location, type);
    }
}
