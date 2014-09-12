package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;

public final class CollectItemMessage implements Message {

    private final int id, collector;

    public CollectItemMessage(int id, int collector) {
        this.id = id;
        this.collector = collector;
    }

    public int getId() {
        return id;
    }

    public int getCollector() {
        return collector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollectItemMessage that = (CollectItemMessage) o;

        if (collector != that.collector) return false;
        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + collector;
        return result;
    }

    @Override
    public String toString() {
        return "CollectItemMessage{id=" + id + ",collector=" + collector + "}";
    }
}
