package net.glowstone.entity;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;

import org.bukkit.entity.Sheep;
import org.bukkit.DyeColor;

public class GlowSheep extends GlowAnimals implements Sheep {

    private boolean sheared = false;

    public GlowSheep(GlowServer server, GlowWorld world) {
        super(server, world, 91);
    }

    public boolean isSheared() {
        return sheared;
    }

    public void setSheared(boolean flag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public DyeColor getColor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setColor(DyeColor color) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
