package net.glowstone.net.message.status;

import net.glowstone.net.flow.AsyncableMessage;
import lombok.Data;

@Data
public final class StatusPingMessage implements AsyncableMessage {

    private final long time;

    @Override
    public boolean isAsync() {
        return true;
    }

}
