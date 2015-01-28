package net.glowstone.net.message.login;

import net.glowstone.net.flow.Message;
import lombok.Data;

@Data
public final class EncryptionKeyRequestMessage implements Message {

    private final String sessionId;
    private final byte[] publicKey;
    private final byte[] verifyToken;

}
