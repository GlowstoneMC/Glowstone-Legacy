package net.glowstone.net.message;

import com.flowpowered.networking.Message;
import net.glowstone.util.TextMessage;

public final class KickMessage implements Message {

    private final TextMessage text;

    public KickMessage(TextMessage text) {
        this.text = text;
    }

    public KickMessage(String text) {
        this.text = new TextMessage(text);
    }

    public TextMessage getText() {
        return text;
    }

    @Override
    public String toString() {
        return "KickMessage" + text.encode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KickMessage that = (KickMessage) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }
}
