package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public final class PingMessage implements Message {

    private final int pingId;

    public PingMessage(int pingId) {
        this.pingId = pingId;
    }

    public int getPingId() {
        return pingId;
    }

    @Override
    public String toString() {
        return "PingMessage{id=" + pingId + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PingMessage that = (PingMessage) o;

        if (pingId != that.pingId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pingId;
    }
}
