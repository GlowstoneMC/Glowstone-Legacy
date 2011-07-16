package net.glowstone.entity;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;

import org.bukkit.entity.Chicken;

/**
 * Represents a chicken.
 * @author Zhuowei Zhang
 */
public final class GlowChicken extends GlowAnimals implements Chicken {

    /**
     * Creates a new chicken.
     * @param server The server this chicken is in.
     * @param world The world this chicken is in.
     */
    public GlowChicken(GlowServer server, GlowWorld world) {
        super(server, world, 93);
    }

}
