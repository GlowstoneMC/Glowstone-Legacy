package net.glowstone.net.message.handshake;

import net.glowstone.net.flow.AsyncableMessage;
import lombok.Data;

@Data
public final class HandshakeMessage implements AsyncableMessage {

    private final int version;
    private final String address;
    private final int port;
    private final int state;

    @Override
    public boolean isAsync() {
        return true;
    }

}
