package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public final class TimeMessage implements Message {

    private final long worldAge, time;

    public TimeMessage(long worldAge, long time) {
        this.worldAge = worldAge;
        this.time = time;
    }

    public long getWorldAge() {
        return worldAge;
    }

    public long getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeMessage that = (TimeMessage) o;

        if (time != that.time) return false;
        if (worldAge != that.worldAge) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (worldAge ^ (worldAge >>> 32));
        result = 31 * result + (int) (time ^ (time >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "TimeMessage{worldAge=" + worldAge + ",time=" + time + "}";
    }

}
