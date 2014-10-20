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
        Location finalLoc = newLoc;
        message.update(newLoc);

        // don't let players move more than 16 blocks in a single packet.
        // this is NOT robust hack prevention - only to prevent client
        // confusion about where its actual location is (e.g. during login)
        if (newLoc.distanceSquared(originLoc) > 16 * 16) {
            return;
        }

        if (!originLoc.equals(newLoc)) {
            session.getPlayer().sendMessage("before " + newLoc.toString());
            final PlayerMoveEvent event = EventFactory.callEvent(new PlayerMoveEvent(session.getPlayer(), originLoc, newLoc));
            session.getPlayer().sendMessage("after  " + newLoc.toString());

            if (event.isCancelled()) {
                finalLoc = originLoc;
            } else if (!event.getTo().equals(newLoc)) {
                finalLoc = event.getTo();
            }

            if (!newLoc.equals(finalLoc)) {
                session.send(new PositionRotationMessage(finalLoc, session.getPlayer().getEyeHeight(true)));
            }
        }


        // do stuff with onGround if we need to
        session.getPlayer().setRawLocation(finalLoc);
    }
}
