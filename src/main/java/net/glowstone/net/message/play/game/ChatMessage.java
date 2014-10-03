package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import net.glowstone.util.TextMessage;
import org.json.simple.JSONObject;

public final class ChatMessage implements Message {

    private final TextMessage text;
    private final int mode;

    public ChatMessage(TextMessage text, int mode) {
        this.text = text;
        this.mode = mode;
    }

    public ChatMessage(JSONObject json) {
        this(new TextMessage(json), 0);
    }

    public ChatMessage(String text) {
        this(new TextMessage(text), 0);
    }

    public TextMessage getText() {
        return text;
    }

    public int getMode() {
        return mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatMessage that = (ChatMessage) o;

        if (mode != that.mode) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + mode;
        return result;
    }
}
