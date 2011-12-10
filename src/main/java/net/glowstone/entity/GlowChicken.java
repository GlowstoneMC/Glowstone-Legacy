package net.glowstone.entity;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;

import org.bukkit.entity.Chicken;

public class GlowChicken extends GlowAnimal implements Chicken {

    private static final int type = 93;

    /**
     * Creates a new animal.
     * @param world The world this monster is in.
     */
    public GlowChicken(GlowServer server, GlowWorld world) {
        super(server, world, type);
    }

    /**
     * Gets the type of creature.
     * @return The type of creature.
     */
    public int getType() {
        return type;
    }

}
