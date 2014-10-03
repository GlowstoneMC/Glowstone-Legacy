package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import org.bukkit.Location;

/**
 * Base class for player update messages.
 */
public class PlayerUpdateMessage implements Message {

    private final boolean onGround;

    public PlayerUpdateMessage(boolean onGround) {
        this.onGround = onGround;
    }

    public final boolean getOnGround() {
        return onGround;
    }

    public void update(Location location) {
        // do nothing
    }

    @Override
    public String toString() {
        return "PlayerUpdateMessage{" +
                "onGround=" + onGround +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerUpdateMessage that = (PlayerUpdateMessage) o;

        if (onGround != that.onGround) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (onGround ? 1 : 0);
    }
}
