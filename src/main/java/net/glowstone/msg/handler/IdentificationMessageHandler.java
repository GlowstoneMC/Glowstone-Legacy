package net.glowstone.msg.handler;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.msg.IdentificationMessage;
import net.glowstone.net.Session;
import net.glowstone.net.Session.State;

public final class IdentificationMessageHandler extends MessageHandler<IdentificationMessage> {

    @Override
    public void handle(Session session, GlowPlayer player, IdentificationMessage message) {
        Session.State state = session.getState();
        if (state == Session.State.EXCHANGE_IDENTIFICATION) {
            session.setState(State.GAME);
            session.send(new IdentificationMessage(0, "", 0, 0));
            session.setPlayer(new GlowPlayer(session, message.getName())); // TODO case-correct the name
        } else {
            boolean game = state == State.GAME;
            session.disconnect(game ? "Identification already exchanged." : "Handshake not yet exchanged.");
        }
    }

}
