package net.glowstone.entity;

import org.bukkit.entity.Animals;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;

public abstract class GlowAnimals extends GlowCreature implements Animals{
    public GlowAnimals(GlowServer server, GlowWorld world, int type) {
        super(server, world, type);
    }
}
