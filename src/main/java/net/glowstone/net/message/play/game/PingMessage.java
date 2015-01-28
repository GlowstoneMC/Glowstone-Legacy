package net.glowstone.net.message.play.game;

import net.glowstone.net.flow.AsyncableMessage;
import lombok.Data;

@Data
public final class PingMessage implements AsyncableMessage {

    private final int pingId;

    @Override
    public boolean isAsync() {
        return true;
    }
}
