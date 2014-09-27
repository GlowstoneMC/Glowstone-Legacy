package net.glowstone.network.handler.play.game;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.entity.meta.ClientSettings;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.game.ClientSettingsMessage;

public final class ClientSettingsHandler implements MessageHandler<GlowSession, ClientSettingsMessage> {
    @Override
    public void handle(GlowSession session, ClientSettingsMessage message) {
        session.getPlayer().setSettings(new ClientSettings(message));
    }
}
