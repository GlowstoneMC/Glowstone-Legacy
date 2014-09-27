package net.glowstone.network.handler.play.player;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.GlowServer;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.player.SteerVehicleMessage;

public final class SteerVehicleHandler implements MessageHandler<GlowSession, SteerVehicleMessage> {
    @Override
    public void handle(GlowSession session, SteerVehicleMessage message) {
        // todo
        GlowServer.logger.info(session + ": " + message);
    }
}
