package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import org.bukkit.Location;

public final class PositionRotationMessage implements Message {

    private final double x, y, z;
    private final float yaw, pitch;
    private final int flags;


    public PositionRotationMessage(double x, double y, double z, float yaw, float pitch) {
        this(x, y, z, yaw, pitch, 0);
    }

    public PositionRotationMessage(double x, double y, double z, float yaw, float pitch, int flags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
    }

    public PositionRotationMessage(Location location, double yOffset) {
        this(location.getX(), location.getY() + yOffset, location.getZ(), location.getYaw(), location.getPitch());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public String toString() {
        return "PositionRotationMessage{x=" + x + ",y=" + y + ",z=" + z +
                ",rotation=" + yaw + ",pitch=" +
                pitch + ",flags=" + flags + "}";
    }
}
