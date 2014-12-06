package net.glowstone.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;

/**
 * Represents a monster such as a creeper.
 */
public class GlowMonster extends GlowCreature implements Monster {

    /**
     * Creates a new monster.
     * @param location The location of the monster.
     * @param type The type of monster.
     */
    public GlowMonster(Location location, EntityType type) {
        super(location, type);
    }
}
