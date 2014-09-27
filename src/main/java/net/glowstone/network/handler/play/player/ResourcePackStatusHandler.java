package net.glowstone.network.handler.play.player;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.GlowServer;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.player.ResourcePackStatusMessage;

public final class ResourcePackStatusHandler implements MessageHandler<GlowSession, ResourcePackStatusMessage> {
    @Override
    public void handle(GlowSession session, ResourcePackStatusMessage message) {
        GlowServer.logger.info(session + ": " + message);
    }
}
