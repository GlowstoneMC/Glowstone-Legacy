package net.glowstone.net.message.status;

import com.flowpowered.networking.AsyncableMessage;

public final class StatusPingMessage implements AsyncableMessage {

    private final long time;

    public StatusPingMessage(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusPingMessage that = (StatusPingMessage) o;

        if (time != that.time) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (time ^ (time >>> 32));
    }

    @Override
    public String toString() {
        return "StatusPingMessage{" +
                "time=" + time +
                '}';
    }
}
