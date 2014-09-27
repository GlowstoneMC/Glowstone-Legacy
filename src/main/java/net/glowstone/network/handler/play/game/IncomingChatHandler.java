package net.glowstone.network.handler.play.game;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.game.IncomingChatMessage;

public final class IncomingChatHandler implements MessageHandler<GlowSession, IncomingChatMessage> {

    @Override
    public void handle(GlowSession session, IncomingChatMessage message) {
        session.getPlayer().chat(message.getText(), message.isAsync());
    }
}
