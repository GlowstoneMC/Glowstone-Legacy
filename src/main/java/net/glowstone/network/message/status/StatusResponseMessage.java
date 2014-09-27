package net.glowstone.network.message.status;

import net.glowstone.network.message.JsonMessage;
import org.json.simple.JSONObject;

public final class StatusResponseMessage extends JsonMessage {

    public StatusResponseMessage(JSONObject json) {
        super(json);
    }

}
