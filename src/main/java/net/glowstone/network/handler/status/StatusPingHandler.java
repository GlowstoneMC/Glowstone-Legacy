package net.glowstone.network.handler.status;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.status.StatusPingMessage;

public final class StatusPingHandler implements MessageHandler<GlowSession, StatusPingMessage> {

    @Override
    public void handle(GlowSession session, StatusPingMessage message) {
        session.send(message);
    }
}
