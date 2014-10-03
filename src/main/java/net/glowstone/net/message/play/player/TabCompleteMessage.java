package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import org.bukkit.util.BlockVector;

public final class TabCompleteMessage implements Message {

    private final String text;
    private final BlockVector location;

    public TabCompleteMessage(String text) {
        this(text, null);
    }

    public TabCompleteMessage(String text, BlockVector location) {
        this.text = text;
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public BlockVector getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TabCompleteMessage that = (TabCompleteMessage) o;

        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TabCompleteMessage{" +
                "text='" + text + '\'' +
                ", location=" + location +
                '}';
    }
}

