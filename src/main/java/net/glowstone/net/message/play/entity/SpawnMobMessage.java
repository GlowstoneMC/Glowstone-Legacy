package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import net.glowstone.entity.meta.MetadataMap;

import java.util.List;

public final class SpawnMobMessage implements Message {

    private final int id, type, x, y, z, rotation, pitch, headPitch, velX, velY, velZ;
    private final List<MetadataMap.Entry> metadata;

    public SpawnMobMessage(int id, int type, int x, int y, int z, int rotation, int pitch, int headPitch, int velX, int velY, int velZ, List<MetadataMap.Entry> metadata) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.pitch = pitch;
        this.headPitch = headPitch;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
        this.metadata = metadata;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getRotation() {
        return rotation;
    }

    public int getPitch() {
        return pitch;
    }

    public int getHeadPitch() {
        return headPitch;
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }

    public int getVelZ() {
        return velZ;
    }

    public List<MetadataMap.Entry> getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpawnMobMessage that = (SpawnMobMessage) o;

        if (headPitch != that.headPitch) return false;
        if (id != that.id) return false;
        if (pitch != that.pitch) return false;
        if (rotation != that.rotation) return false;
        if (type != that.type) return false;
        if (velX != that.velX) return false;
        if (velY != that.velY) return false;
        if (velZ != that.velZ) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        if (metadata != null ? !metadata.equals(that.metadata) : that.metadata != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + type;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + rotation;
        result = 31 * result + pitch;
        result = 31 * result + headPitch;
        result = 31 * result + velX;
        result = 31 * result + velY;
        result = 31 * result + velZ;
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpawnMobMessage{" +
                "id=" + id +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", rotation=" + rotation +
                ", pitch=" + pitch +
                ", headPitch=" + headPitch +
                ", velX=" + velX +
                ", velY=" + velY +
                ", velZ=" + velZ +
                ", metadata=" + metadata +
                '}';
    }

}
