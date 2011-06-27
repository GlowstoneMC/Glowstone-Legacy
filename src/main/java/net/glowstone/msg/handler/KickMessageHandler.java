package net.glowstone.msg.handler;

import java.util.logging.Level;
import net.glowstone.EventFactory;
import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.msg.KickMessage;
import net.glowstone.net.Session;

public final class KickMessageHandler extends MessageHandler<KickMessage> {

    @Override
    public void handle(Session session, GlowPlayer player, KickMessage message) {
        GlowServer.logger.log(Level.INFO, "{0} left the game", player.getName());

        String text = EventFactory.onPlayerQuit(player).getQuitMessage();
        if (message != null) {
            session.getServer().broadcastMessage(text);
        }

        session.disconnect("Goodbye!");
    }

}
