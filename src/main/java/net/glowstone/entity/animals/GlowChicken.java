package net.glowstone.entity.animals;

import net.glowstone.entity.GlowAnimal;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;

/**
 * Represents a chicken.
 */
public class GlowChicken extends GlowAnimal implements Chicken {

    /**
     * Creates a new ageable animal.
     * @param location The location of the monster.
     */
    public GlowChicken(Location location) {
        super(location, EntityType.CHICKEN);
    }
}