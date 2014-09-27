package net.glowstone.network.handler.play.player;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.player.PlayerUpdateMessage;
import org.bukkit.Location;

public final class PlayerUpdateHandler implements MessageHandler<GlowSession, PlayerUpdateMessage> {

    @Override
    public void handle(GlowSession session, PlayerUpdateMessage message) {
        Location original = session.getPlayer().getLocation();
        Location newLoc = original.clone();
        message.update(newLoc);

        // don't let players move more than 16 blocks in a single packet.
        // this is NOT robust hack prevention - only to prevent client
        // confusion about where its actual location is (e.g. during login)
        if (newLoc.distanceSquared(original) > 16 * 16) {
            return;
        }

        // do stuff with onGround if we need to
        session.getPlayer().setRawLocation(newLoc);
    }
}
