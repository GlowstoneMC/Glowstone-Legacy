package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;

public final class SpawnObjectMessage implements Message {

    private final int id, type, x, y, z, pitch, yaw, data, velX, velY, velZ;

    public SpawnObjectMessage(int id, int type, int x, int y, int z, int pitch, int yaw) {
        this(id, type, x, y, z, pitch, yaw, 0, 0, 0, 0);
    }

    public SpawnObjectMessage(int id, int type, int x, int y, int z, int pitch, int yaw, int data, int velX, int velY, int velZ) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.data = data;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
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

    public int getPitch() {
        return pitch;
    }

    public int getYaw() {
        return yaw;
    }

    public boolean hasFireball() {
        return data != 0;
    }

    public int getData() {
        return data;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpawnObjectMessage that = (SpawnObjectMessage) o;

        if (data != that.data) return false;
        if (id != that.id) return false;
        if (pitch != that.pitch) return false;
        if (type != that.type) return false;
        if (velX != that.velX) return false;
        if (velY != that.velY) return false;
        if (velZ != that.velZ) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (yaw != that.yaw) return false;
        if (z != that.z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + type;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + pitch;
        result = 31 * result + yaw;
        result = 31 * result + data;
        result = 31 * result + velX;
        result = 31 * result + velY;
        result = 31 * result + velZ;
        return result;
    }

    @Override
    public String toString() {
        return "SpawnObjectMessage{" +
                "id=" + id +
                ", type=" + type +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", pitch=" + pitch +
                ", yaw=" + yaw +
                ", data=" + data +
                ", velX=" + velX +
                ", velY=" + velY +
                ", velZ=" + velZ +
                '}';
    }
}
