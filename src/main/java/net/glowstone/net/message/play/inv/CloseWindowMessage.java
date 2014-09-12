package net.glowstone.net.message.play.inv;

import com.flowpowered.networking.Message;

public final class CloseWindowMessage implements Message {

    private final int id;

    public CloseWindowMessage(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CloseWindowMessage that = (CloseWindowMessage) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "CloseWindowMessage{id=" + id + "}";
    }
}
