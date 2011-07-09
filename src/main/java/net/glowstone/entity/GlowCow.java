package net.glowstone.entity;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;

import org.bukkit.entity.Cow;

/**
 * Represents a cow.
 * @author Zhuowei Zhang
 */
public class GlowCow extends GlowAnimals implements Cow {

    /**
     * Creates a new cow.
     * @param server The server this cow is in.
     * @param world The world this cow is in.
     */
    public GlowCow(GlowServer server, GlowWorld world) {
        super(server, world, 92);
    }

}
