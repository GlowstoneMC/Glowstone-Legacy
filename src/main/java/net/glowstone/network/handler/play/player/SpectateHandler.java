package net.glowstone.network.handler.play.player;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.GlowServer;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.player.SpectateMessage;

public final class SpectateHandler implements MessageHandler<GlowSession, SpectateMessage> {
    @Override
    public void handle(GlowSession session, SpectateMessage message) {
        GlowServer.logger.info(session + ": " + message);
    }
}
