package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;

public final class ResourcePackStatusMessage implements Message {

    private final String hash;
    private final int result;

    public ResourcePackStatusMessage(String hash, int result) {
        this.hash = hash;
        this.result = result;
    }

    public String getHash() {
        return hash;
    }

    public int getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourcePackStatusMessage that = (ResourcePackStatusMessage) o;

        if (result != that.result) return false;
        if (hash != null ? !hash.equals(that.hash) : that.hash != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result1 = hash != null ? hash.hashCode() : 0;
        result1 = 31 * result1 + result;
        return result1;
    }

    @Override
    public String toString() {
        return "ResourcePackStatusMessage{" +
                "hash='" + hash + '\'' +
                ", result=" + result +
                '}';
    }
}

