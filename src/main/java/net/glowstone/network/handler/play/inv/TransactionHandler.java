package net.glowstone.network.handler.play.inv;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.inv.TransactionMessage;

public final class TransactionHandler implements MessageHandler<GlowSession, TransactionMessage> {
    @Override
    public void handle(GlowSession session, TransactionMessage message) {
        //GlowServer.logger.info(session + ": " + message);
    }
}
