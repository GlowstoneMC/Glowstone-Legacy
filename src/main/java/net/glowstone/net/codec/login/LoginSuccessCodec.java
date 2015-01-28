package net.glowstone.net.codec.login;

import net.glowstone.net.flow.Codec;
import net.glowstone.net.flow.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.login.LoginSuccessMessage;

import java.io.IOException;

public final class LoginSuccessCodec implements Codec<LoginSuccessMessage> {
    @Override
    public LoginSuccessMessage decode(ByteBuf buffer) throws IOException {
        String uuid = ByteBufUtils.readUTF8(buffer);
        String username = ByteBufUtils.readUTF8(buffer);

        return new LoginSuccessMessage(uuid, username);
    }

    @Override
    public ByteBuf encode(ByteBuf buf, LoginSuccessMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buf, message.getUuid());
        ByteBufUtils.writeUTF8(buf, message.getUsername());
        return buf;
    }
}
