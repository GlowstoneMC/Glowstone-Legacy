package net.glowstone.network.codec.status;

import com.flowpowered.networking.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.network.message.status.StatusRequestMessage;

import java.io.IOException;

public final class StatusRequestCodec implements Codec<StatusRequestMessage> {
    @Override
    public StatusRequestMessage decode(ByteBuf byteBuf) throws IOException {
        return new StatusRequestMessage();
    }

    @Override
    public ByteBuf encode(ByteBuf buf, StatusRequestMessage statusRequestMessage) throws IOException {
        return buf;
    }
}
