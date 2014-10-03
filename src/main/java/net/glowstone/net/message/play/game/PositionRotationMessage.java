package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import org.bukkit.Location;

public final class PositionRotationMessage implements Message {

    private final double x, y, z;
    private final float rotation, pitch;
    private final int flags;

    public PositionRotationMessage(double x, double y, double z, float rotation, float pitch) {
        this(x, y, z, rotation, pitch, 0);
    }

    public PositionRotationMessage(double x, double y, double z, float rotation, float pitch, int flags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
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

    public float getRotation() {
        return rotation;
    }

    public float getPitch() {
        return pitch;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PositionRotationMessage that = (PositionRotationMessage) o;

        if (flags != that.flags) return false;
        if (Float.compare(that.pitch, pitch) != 0) return false;
        if (Float.compare(that.rotation, rotation) != 0) return false;
        if (Double.compare(that.x, x) != 0) return false;
        if (Double.compare(that.y, y) != 0) return false;
        if (Double.compare(that.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (rotation != +0.0f ? Float.floatToIntBits(rotation) : 0);
        result = 31 * result + (pitch != +0.0f ? Float.floatToIntBits(pitch) : 0);
        result = 31 * result + flags;
        return result;
    }

    @Override
    public String toString() {
        return "PositionRotationMessage{x=" + x + ",y=" + y + ",z=" + z +
                ",rotation=" + rotation + ",pitch=" +
                pitch + ",flags=" + flags + "}";
    }
}
