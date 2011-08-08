package net.glowstone.msg.handler;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.msg.SpoutMessage;
import net.glowstone.net.Session;

/**
 * A {@link MessageHandler} which processes Spoutcraft packets.
 */
public final class SpoutMessageHandler extends MessageHandler<SpoutMessage> {

    @Override
    public void handle(Session session, GlowPlayer player, SpoutMessage message) {
        if (player == null)
            return;

        message.getPacket().run(player.getEntityId());
    }

}
