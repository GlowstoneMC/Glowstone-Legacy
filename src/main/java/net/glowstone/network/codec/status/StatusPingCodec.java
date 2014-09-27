package net.glowstone.network.codec.status;

import com.flowpowered.networking.Codec;
import io.netty.buffer.ByteBuf;
import net.glowstone.network.message.status.StatusPingMessage;

import java.io.IOException;

public final class StatusPingCodec implements Codec<StatusPingMessage> {
    @Override
    public StatusPingMessage decode(ByteBuf byteBuf) throws IOException {
        return new StatusPingMessage(byteBuf.readLong());
    }

    @Override
    public ByteBuf encode(ByteBuf byteBuf, StatusPingMessage statusPingMessage) throws IOException {
        byteBuf.writeLong(statusPingMessage.getTime());
        return byteBuf;
    }
}
