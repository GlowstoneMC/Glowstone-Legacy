package net.glowstone.net.handler.play.player;

import net.glowstone.net.flow.MessageHandler;
import net.glowstone.GlowServer;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.player.SteerVehicleMessage;

public final class SteerVehicleHandler implements MessageHandler<GlowSession, SteerVehicleMessage> {
    @Override
    public void handle(GlowSession session, SteerVehicleMessage message) {
        // todo
        GlowServer.logger.info(session + ": " + message);
    }
}
