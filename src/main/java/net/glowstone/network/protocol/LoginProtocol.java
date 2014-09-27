package net.glowstone.network.protocol;

import net.glowstone.network.codec.JsonCodec;
import net.glowstone.network.codec.SetCompressionCodec;
import net.glowstone.network.codec.login.EncryptionKeyRequestCodec;
import net.glowstone.network.codec.login.EncryptionKeyResponseCodec;
import net.glowstone.network.codec.login.LoginStartCodec;
import net.glowstone.network.codec.login.LoginSuccessCodec;
import net.glowstone.network.handler.login.EncryptionKeyResponseHandler;
import net.glowstone.network.handler.login.LoginStartHandler;
import net.glowstone.network.message.KickMessage;
import net.glowstone.network.message.SetCompressionMessage;
import net.glowstone.network.message.login.EncryptionKeyRequestMessage;
import net.glowstone.network.message.login.EncryptionKeyResponseMessage;
import net.glowstone.network.message.login.LoginStartMessage;
import net.glowstone.network.message.login.LoginSuccessMessage;

public final class LoginProtocol extends GlowProtocol {
    public LoginProtocol() {
        super("LOGIN", 5);

        inbound(0x00, LoginStartMessage.class, LoginStartCodec.class, LoginStartHandler.class);
        inbound(0x01, EncryptionKeyResponseMessage.class, EncryptionKeyResponseCodec.class, EncryptionKeyResponseHandler.class);

        outbound(0x00, KickMessage.class, JsonCodec.class);
        outbound(0x01, EncryptionKeyRequestMessage.class, EncryptionKeyRequestCodec.class);
        outbound(0x02, LoginSuccessMessage.class, LoginSuccessCodec.class);
        outbound(0x03, SetCompressionMessage.class, SetCompressionCodec.class);
    }
}
