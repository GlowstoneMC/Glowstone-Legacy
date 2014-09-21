package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;

public class ResourcePackSendMessage implements Message {

    private final String url, hash;

    public ResourcePackSendMessage(String url, String hash) {
        this.url = url;
        this.hash = hash;
    }

    public String getUrl() {
        return url;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return "ResourcePackSendMessage{" +
                "url='" + url + '\'' +
                ", hash='" + hash + '\'' +
                '}';
    }
}
