package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

import java.util.UUID;

public final class SpectateMessage implements Message {

    private final UUID target;

    public SpectateMessage(UUID target) {
        this.target = target;
    }

    public UUID getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpectateMessage that = (SpectateMessage) o;

        if (target != null ? !target.equals(that.target) : that.target != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return target != null ? target.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SpectateMessage{" +
                "target=" + target +
                '}';
    }
}

