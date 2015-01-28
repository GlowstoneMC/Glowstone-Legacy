package net.glowstone.net.message.login;

import net.glowstone.net.flow.AsyncableMessage;
import lombok.Data;

@Data
public final class EncryptionKeyResponseMessage implements AsyncableMessage {

    private final byte[] sharedSecret;
    private final byte[] verifyToken;

    @Override
    public boolean isAsync() {
        return true;
    }

}
