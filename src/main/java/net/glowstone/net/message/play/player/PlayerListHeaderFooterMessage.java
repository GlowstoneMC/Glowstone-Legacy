package net.glowstone.net.message.play.player;

import com.flowpowered.networking.Message;
import org.json.simple.JSONObject;

public class PlayerListHeaderFooterMessage implements Message {

    private JSONObject header, footer;

    public PlayerListHeaderFooterMessage(JSONObject header, JSONObject footer) {
        this.header = header;
        this.footer = footer;
    }

    public JSONObject getHeader() {
        return header;
    }

    public void setHeader(JSONObject header) {
        this.header = header;
    }

    public JSONObject getFooter() {
        return footer;
    }

    public void setFooter(JSONObject footer) {
        this.footer = footer;
    }

    @Override
    public String toString() {
        return "PlayerListHeaderFooterCodec{" +
                "header=" + header +
                ", footer=" + footer +
                '}';
    }
}
