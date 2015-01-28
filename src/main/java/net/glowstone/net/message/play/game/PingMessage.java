package net.glowstone.net.message.play.game;

import com.flowpowered.networking.AsyncableMessage;
import lombok.Data;

@Data
public final class PingMessage implements AsyncableMessage {

    private final int pingId;

    @Override
    public boolean isAsync() {
        return true;
    }
}
