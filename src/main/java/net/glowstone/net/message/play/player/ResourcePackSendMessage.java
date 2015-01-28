package net.glowstone.net.message.play.player;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class ResourcePackSendMessage implements Message {

    private final String url, hash;

}
