package net.glowstone.network.protocol;

import net.glowstone.network.codec.JsonCodec;
import net.glowstone.network.codec.status.StatusPingCodec;
import net.glowstone.network.codec.status.StatusRequestCodec;
import net.glowstone.network.handler.status.StatusPingHandler;
import net.glowstone.network.handler.status.StatusRequestHandler;
import net.glowstone.network.message.status.StatusPingMessage;
import net.glowstone.network.message.status.StatusRequestMessage;
import net.glowstone.network.message.status.StatusResponseMessage;

public final class StatusProtocol extends GlowProtocol {
    public StatusProtocol() {
        super("STATUS", 2);

        inbound(0x00, StatusRequestMessage.class, StatusRequestCodec.class, StatusRequestHandler.class);
        inbound(0x01, StatusPingMessage.class, StatusPingCodec.class, StatusPingHandler.class);

        outbound(0x00, StatusResponseMessage.class, JsonCodec.class);
        outbound(0x01, StatusPingMessage.class, StatusPingCodec.class);
    }
}
