package net.glowstone.network.protocol;

import net.glowstone.network.codec.handshake.HandshakeCodec;
import net.glowstone.network.handler.handshake.HandshakeHandler;
import net.glowstone.network.message.handshake.HandshakeMessage;

public final class HandshakeProtocol extends GlowProtocol {
    public HandshakeProtocol() {
        super("HANDSHAKE", 0);
        inbound(0x00, HandshakeMessage.class, HandshakeCodec.class, HandshakeHandler.class);
    }
}
