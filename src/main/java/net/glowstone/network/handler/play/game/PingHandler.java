package net.glowstone.network.handler.play.game;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.game.PingMessage;

public final class PingHandler implements MessageHandler<GlowSession, PingMessage> {

    @Override
    public void handle(GlowSession session, PingMessage message) {
        session.pong(message.getPingId());
    }
}
