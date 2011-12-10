package net.glowstone.entity;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;

import org.bukkit.entity.Animals;

public class GlowAnimal extends GlowCreature implements Animals {

    /**
     * Creates a new animal.
     * @param world The world this monster is in.
     * @param type The type of monster.
     */
    public GlowAnimal(GlowServer server, GlowWorld world, int type) {
        super(server, world, type);
    }

}
