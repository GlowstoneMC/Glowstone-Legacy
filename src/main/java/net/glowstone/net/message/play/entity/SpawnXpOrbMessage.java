package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;

public final class SpawnXpOrbMessage implements Message {
    private final int id, x, y, z;
    private final short count;

    public SpawnXpOrbMessage(int id, int x, int y, int z, short count) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.count = count;
    }

    public int getId() {
        return id;
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

    public short getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpawnXpOrbMessage that = (SpawnXpOrbMessage) o;

        if (count != that.count) return false;
        if (id != that.id) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + (int) count;
        return result;
    }

    @Override
    public String toString() {
        return "SpawnXpOrbMessage{id=" + id + ",x=" + x + ",y=" + y + ",z=" + z + ",count=" + count + "}";
    }
}
