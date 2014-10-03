package net.glowstone.net.message.play.game;

import com.flowpowered.networking.AsyncableMessage;

public final class IncomingChatMessage implements AsyncableMessage {

    private final String text;

    public IncomingChatMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IncomingChatMessage that = (IncomingChatMessage) o;

        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "IncomingChatMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}
