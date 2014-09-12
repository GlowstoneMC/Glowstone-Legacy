package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;

public final class SpawnPaintingMessage implements Message {

    private final int id, x, y, z, facing;
    private final String title;

    public SpawnPaintingMessage(int id, String title, int x, int y, int z, int facing) {
        this.id = id;
        this.title = title;
        this.x = x;
        this.y = y;
        this.z = z;
        this.facing = facing;
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

    public int getFacing() {
        return facing;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpawnPaintingMessage that = (SpawnPaintingMessage) o;

        if (facing != that.facing) return false;
        if (id != that.id) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + facing;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SpawnPaintingMessage{id=" + id + ",x=" + x + ",y=" + y + ",z=" + z + ",facing=" + facing + ",title=" + title + "}";
    }
}
