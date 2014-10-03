package net.glowstone.net.message.login;

import com.flowpowered.networking.Message;

import java.util.Arrays;

public final class EncryptionKeyRequestMessage implements Message {

    private final String sessionId;
    private final byte[] publicKey;
    private final byte[] verifyToken;

    public EncryptionKeyRequestMessage(String sessionId, byte[] publicKey, byte[] verifyToken) {
        this.sessionId = sessionId;
        this.publicKey = publicKey;
        this.verifyToken = verifyToken;
    }

    public String getSessionId() {
        return sessionId;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public byte[] getVerifyToken() {
        return verifyToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EncryptionKeyRequestMessage that = (EncryptionKeyRequestMessage) o;

        if (!Arrays.equals(publicKey, that.publicKey)) return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (!Arrays.equals(verifyToken, that.verifyToken)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (publicKey != null ? Arrays.hashCode(publicKey) : 0);
        result = 31 * result + (verifyToken != null ? Arrays.hashCode(verifyToken) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EncryptionKeyRequestMessage{" +
                "sessionId='" + sessionId + '\'' +
                ", publicKey=" + Arrays.toString(publicKey) +
                ", verifyToken=" + Arrays.toString(verifyToken) +
                '}';
    }
}
