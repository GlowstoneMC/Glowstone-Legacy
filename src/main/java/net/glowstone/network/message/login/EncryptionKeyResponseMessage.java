package net.glowstone.network.message.login;


import com.flowpowered.networking.Message;

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

}
