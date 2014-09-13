package net.glowstone.net.handler.play.player;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.EventFactory;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.game.PositionRotationMessage;
import net.glowstone.net.message.play.player.PlayerUpdateMessage;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

public final class PlayerUpdateHandler implements MessageHandler<GlowSession, PlayerUpdateMessage> {

    @Override
    public void handle(GlowSession session, PlayerUpdateMessage message) {
        Location originLoc = session.getPlayer().getLocation();
        Location newLoc = originLoc.clone();
        message.update(newLoc);

        // don't let players move more than 16 blocks in a single packet.
        // this is NOT robust hack prevention - only to prevent client
        // confusion about where its actual location is (e.g. during login)
        if (newLoc.distanceSquared(originLoc) > 16 * 16) {
            return;
        }

        final PlayerMoveEvent event = EventFactory.onPlayerMove(session.getPlayer(), originLoc, newLoc);
        if (event.isCancelled()) {
            // Magic value
            // https://github.com/JeromSar/CraftBukkit/blob/master/src/main/java/net/minecraft/server/PlayerConnection.java#L240
            session.send(new PositionRotationMessage(originLoc, 1.6200000047683716D)); // Magic value:
            return;
        }

        // do stuff with onGround if we need to
        session.getPlayer().setRawLocation(newLoc);
    }
}
