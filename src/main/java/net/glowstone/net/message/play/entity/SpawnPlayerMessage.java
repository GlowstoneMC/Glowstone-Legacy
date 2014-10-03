package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;
import net.glowstone.entity.meta.MetadataMap;

import java.util.List;
import java.util.UUID;

public final class SpawnPlayerMessage implements Message {

    private final int id;
    private final UUID uuid;
    private final int x, y, z;
    private final int rotation, pitch;
    private final int item;
    private final List<MetadataMap.Entry> metadata;

    public SpawnPlayerMessage(int id, UUID uuid, int x, int y, int z, int rotation, int pitch, int item, List<MetadataMap.Entry> metadata) {
        this.id = id;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotation = rotation;
        this.pitch = pitch;
        this.item = item;
        this.metadata = metadata;
    }

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
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

    public int getItem() {
        return item;
    }

    public List<MetadataMap.Entry> getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpawnPlayerMessage that = (SpawnPlayerMessage) o;

        if (id != that.id) return false;
        if (item != that.item) return false;
        if (pitch != that.pitch) return false;
        if (rotation != that.rotation) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        if (z != that.z) return false;
        if (metadata != null ? !metadata.equals(that.metadata) : that.metadata != null) return false;
        if (uuid != null ? !uuid.equals(that.uuid) : that.uuid != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        result = 31 * result + rotation;
        result = 31 * result + pitch;
        result = 31 * result + item;
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "SpawnPlayerMessage{" +
                "id=" + id +
                ", uuid=" + uuid +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", rotation=" + rotation +
                ", pitch=" + pitch +
                ", item=" + item +
                ", metadata=" + metadata +
                '}';
    }

}
