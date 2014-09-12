package net.glowstone.net.message.play.entity;

import com.flowpowered.networking.Message;

import java.util.Arrays;
import java.util.List;

public final class DestroyEntitiesMessage implements Message {

    private final List<Integer> ids;

    public DestroyEntitiesMessage(Integer... ids) {
        this.ids = Arrays.asList(ids);
    }

    public DestroyEntitiesMessage(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getIds() {
        return ids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DestroyEntitiesMessage that = (DestroyEntitiesMessage) o;

        if (ids != null ? !ids.equals(that.ids) : that.ids != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ids != null ? ids.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DestroyEntitiesMessage{ids=" + ids + "}";
    }

}
