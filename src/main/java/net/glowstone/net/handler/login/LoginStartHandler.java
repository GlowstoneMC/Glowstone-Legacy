package net.glowstone.net.handler.login;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.entity.meta.PlayerProfile;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.login.EncryptionKeyRequestMessage;
import net.glowstone.net.message.login.LoginStartMessage;
import net.glowstone.util.SecurityUtils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class LoginStartHandler implements MessageHandler<GlowSession, LoginStartMessage> {

    @Override
    public void handle(GlowSession session, LoginStartMessage message) {
        final String name = message.getUsername();

        if (session.getServer().getOnlineMode()) {
            //Get necessary information to create our request message
            final String sessionId = session.getSessionId();
            final byte[] publicKey = SecurityUtils.generateX509Key(session.getServer().getKeyPair().getPublic()).getEncoded(); //Convert to X509 format
            final byte[] verifyToken = SecurityUtils.generateVerifyToken();

            //Set verify data on session for use in the response handler
            session.setVerifyToken(verifyToken);
            session.setVerifyUsername(name);

            //Send created request message and wait for the response
            session.send(new EncryptionKeyRequestMessage(sessionId, publicKey, verifyToken));
        } else {
            UUID uuid = session.getSpoofedUUID();
            if (uuid == null) {
                uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(StandardCharsets.UTF_8));
            }
            
            if (session.getSpoofedProperties() == null) {
                session.setPlayer(new PlayerProfile(name, uuid));
            } else {
                session.setPlayer(new PlayerProfile(name, uuid, session.getSpoofedProperties()));
            }
        }
    }
}
