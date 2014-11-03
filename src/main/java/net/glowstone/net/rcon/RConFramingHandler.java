package net.glowstone.net.rcon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.nio.ByteOrder;
import java.util.List;

public class RConFramingHandler extends ByteToMessageCodec<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        out.writeBytes(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) {
            return;
        }

        in.markReaderIndex();
        int length = in.order(ByteOrder.LITTLE_ENDIAN).readInt();
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }

        in.resetReaderIndex();

        ByteBuf buf = ctx.alloc().buffer(in.readableBytes());
        in.readBytes(buf, in.readableBytes());
        out.add(buf);
    }
}
