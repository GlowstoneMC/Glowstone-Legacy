package net.glowstone.net.rcon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

public class RConFramingHandler extends ByteToMessageCodec<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        System.out.println("Encoding");
        out.writeBytes(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("Frame");
        in.markReaderIndex();


        if (in.readableBytes() < 4) {
            System.out.println("Returning");
            return;
        }

       /* int length = in.readInt();
        System.out.println("Length: " + length);
        System.out.println("Available " + in.readableBytes());
        if (in.readableBytes() < length) {
            System.out.println("Bye");
            in.resetReaderIndex();
            return;
        }*/

        in.resetReaderIndex();

        ByteBuf buf = ctx.alloc().buffer(in.readableBytes());
        in.readBytes(buf, in.readableBytes());
        System.out.println("Passing");
        out.add(buf);
    }
}
