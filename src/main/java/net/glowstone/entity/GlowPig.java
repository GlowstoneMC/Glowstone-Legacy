package net.glowstone.entity;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;

import org.bukkit.entity.Pig;
import org.bukkit.util.Vector;

public class GlowPig extends GlowAnimals implements Pig {

    private boolean saddleStatus = false;

    public GlowPig(GlowServer server, GlowWorld world) {
        super(server, world, 90);
    }

    public boolean hasSaddle() {
        return saddleStatus;
    }

    public void setSaddle(boolean status) {
        this.saddleStatus = status;
    }

    public Vector getVelocity() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setVelocity(Vector vel) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
