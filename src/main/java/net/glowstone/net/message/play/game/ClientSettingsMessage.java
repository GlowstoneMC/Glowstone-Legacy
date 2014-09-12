package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public final class ClientSettingsMessage implements Message {

    private final String locale;
    private final int viewDistance, chatFlags;
    private final boolean chatColors;
    private final int skinFlags;

    public ClientSettingsMessage(String locale, int viewDistance, int chatFlags, boolean chatColors, int skinFlags) {
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.chatFlags = chatFlags;
        this.chatColors = chatColors;
        this.skinFlags = skinFlags;
    }

    public String getLocale() {
        return locale;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public int getChatFlags() {
        return chatFlags;
    }

    public boolean getChatColors() {
        return chatColors;
    }

    public int getSkinFlags() {
        return skinFlags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientSettingsMessage that = (ClientSettingsMessage) o;

        if (chatColors != that.chatColors) return false;
        if (chatFlags != that.chatFlags) return false;
        if (skinFlags != that.skinFlags) return false;
        if (viewDistance != that.viewDistance) return false;
        if (locale != null ? !locale.equals(that.locale) : that.locale != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = locale != null ? locale.hashCode() : 0;
        result = 31 * result + viewDistance;
        result = 31 * result + chatFlags;
        result = 31 * result + (chatColors ? 1 : 0);
        result = 31 * result + skinFlags;
        return result;
    }

    @Override
    public String toString() {
        return "ClientSettingsMessage{" +
                "locale='" + locale + '\'' +
                ", viewDistance=" + viewDistance +
                ", chatFlags=" + chatFlags +
                ", chatColors=" + chatColors +
                ", skinFlags=" + skinFlags +
                '}';
    }
}
