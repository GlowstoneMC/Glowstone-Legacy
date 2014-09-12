package net.glowstone.net.message.status;

import com.flowpowered.networking.Message;
import org.json.simple.JSONObject;

public final class StatusResponseMessage implements Message {

    private final String json;

    public StatusResponseMessage(JSONObject json) {
        this.json = json.toJSONString();
    }

    public String getJson() {
        return json;
    }

    @Override
    public String toString() {
        return "StatusResponseMessage" + json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusResponseMessage that = (StatusResponseMessage) o;

        if (json != null ? !json.equals(that.json) : that.json != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return json != null ? json.hashCode() : 0;
    }
}
