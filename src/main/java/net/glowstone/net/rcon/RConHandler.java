package net.glowstone.net.rcon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class RConHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final byte TYPE_COMMAND = 2;
    private static final byte TYPE_LOGIN = 3;
    private final String password;

    /**
     * The {@link net.glowstone.net.query.QueryServer} this handler belongs to.
     */
    private RConServer rconServer;

    public RConHandler(RConServer rconServer, String password) {
        this.rconServer = rconServer;
        this.password = password;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        System.out.println("Reading");
        if (buf.readableBytes() < 16) {
            System.out.println("Nope");
            return;
        }

        String payload;

        int length = buf.order(ByteOrder.LITTLE_ENDIAN).readInt();

        // The request ID, type, and 2 byte pad take up 10 bytes in total.
        // Subtracting this from the length of the remainder of the packet
        // yields the length of the payload array.
        int payloadLength = length - 10;

        byte[] payloadData = new byte[payloadLength];

        int requestId = buf.order(ByteOrder.LITTLE_ENDIAN).readInt();
        int type = buf.order(ByteOrder.LITTLE_ENDIAN).readInt();
        buf.readBytes(payloadData);

        payload = new String(payloadData, StandardCharsets.UTF_8);

        buf.readBytes(2); // Two byte padding

        if (type == TYPE_LOGIN) {
            System.out.println("Login");
            handleLogin(ctx, payload, requestId);
        }

    }

    private void handleLogin(ChannelHandlerContext ctx, String password, int requestId) throws IOException {
        ByteBuf buf;

        if (password.equals(this.password)) {
            rconServer.setVerified(requestId, true);

            buf = ctx.alloc().buffer();
            createResponse(buf, requestId, 2, "");
        } else {
            rconServer.setVerified(requestId, false);

            buf = ctx.alloc().buffer();
            createResponse(buf, -1, 2, "");
        }

        System.out.println("Done");

        if (buf != null) {
            System.out.println("Ended");
            ctx.write(buf);
        }
    }

    private void createResponse(ByteBuf buf, int requestId, int type, String payload) throws IOException {
        buf.order(ByteOrder.LITTLE_ENDIAN).writeInt(10 + payload.length());
        buf.order(ByteOrder.LITTLE_ENDIAN).writeInt(requestId);
        buf.order(ByteOrder.LITTLE_ENDIAN).writeInt(type);
        buf.writeBytes(payload.getBytes(StandardCharsets.UTF_8));
        buf.writeByte(0);
        buf.writeByte(0);
    }
}
