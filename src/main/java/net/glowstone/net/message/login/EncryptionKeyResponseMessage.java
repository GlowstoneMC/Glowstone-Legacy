package net.glowstone.net.message.login;


import com.flowpowered.networking.Message;

import java.util.Arrays;

public final class EncryptionKeyResponseMessage implements Message {

    private final byte[] sharedSecret;
    private final byte[] verifyToken;

    public EncryptionKeyResponseMessage(byte[] sharedSecret, byte[] verifyToken) {
        this.sharedSecret = sharedSecret;
        this.verifyToken = verifyToken;
    }

    public byte[] getSharedSecret() {
        return sharedSecret;
    }

    public byte[] getVerifyToken() {
        return verifyToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EncryptionKeyResponseMessage that = (EncryptionKeyResponseMessage) o;

        if (!Arrays.equals(sharedSecret, that.sharedSecret)) return false;
        if (!Arrays.equals(verifyToken, that.verifyToken)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sharedSecret != null ? Arrays.hashCode(sharedSecret) : 0;
        result = 31 * result + (verifyToken != null ? Arrays.hashCode(verifyToken) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EncryptionKeyResponseMessage{" +
                "sharedSecret=" + Arrays.toString(sharedSecret) +
                ", verifyToken=" + Arrays.toString(verifyToken) +
                '}';
    }
}
