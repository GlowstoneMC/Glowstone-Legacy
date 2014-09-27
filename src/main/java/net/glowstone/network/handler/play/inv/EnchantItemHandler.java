package net.glowstone.network.handler.play.inv;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.GlowServer;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.inv.EnchantItemMessage;

public final class EnchantItemHandler implements MessageHandler<GlowSession, EnchantItemMessage> {
    @Override
    public void handle(GlowSession session, EnchantItemMessage message) {
        // todo
        GlowServer.logger.info(session + ": " + message);
    }
}
