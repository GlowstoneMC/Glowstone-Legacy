package net.glowstone.net.rcon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.glowstone.EventFactory;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandException;
import org.bukkit.event.server.RemoteServerCommandEvent;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class RConHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final byte TYPE_RESPONSE = 0;
    private static final byte TYPE_COMMAND = 2;
    private static final byte TYPE_LOGIN = 3;
    private final String password;

    private boolean loggedIn = false;

    /**
     * The {@link net.glowstone.net.query.QueryServer} this handler belongs to.
     */
    private RConServer rconServer;

    /**
     * The {@link net.glowstone.net.rcon.RConCommandSender} for this connection.
     */
    private RConCommandSender commandSender;

    public RConHandler(RConServer rconServer, String password) {
        this.rconServer = rconServer;
        this.password = password;
        this.commandSender = new RConCommandSender(rconServer.getServer());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buf) throws Exception {
        if (buf.readableBytes() < 10) {
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
            handleLogin(ctx, payload, requestId);
        } else if (type == TYPE_COMMAND) {
            handleCommand(ctx, payload, requestId);
        } else {
            sendMultiPacketResponse(ctx, requestId, String.format("Unknown request %s", Integer.toHexString(type)));
        }
    }

    private void handleCommand(ChannelHandlerContext ctx, String payload, int requestId) throws IOException {
        if (this.loggedIn) {
            try {
                EventFactory.callEvent(new RemoteServerCommandEvent(commandSender, payload));
;               rconServer.getServer().dispatchCommand(commandSender, payload);
                sendMultiPacketResponse(ctx, requestId, ChatColor.stripColor(commandSender.getLog()));
                commandSender.clearLog();
            } catch (CommandException e) {
                sendMultiPacketResponse(ctx, requestId, String.format("Error executing: %s (%s)", payload, e.getMessage()));
            }
        } else {
            ByteBuf buf = ctx.alloc().buffer();
            createResponse(buf, -1, 2, "");
            ctx.write(buf);
        }
    }

    private void handleLogin(ChannelHandlerContext ctx, String password, int requestId) throws IOException {
        ByteBuf buf = ctx.alloc().buffer();;

        if (password.equals(this.password)) {
            this.loggedIn = true;
            createResponse(buf, requestId, 2, "");
        } else {
            this.loggedIn = false;
            createResponse(buf, -1, 2, "");
        }
        ctx.write(buf);
    }

    private void createResponse(ByteBuf buf, int requestId, int type, String payload) throws IOException {
        buf.order(ByteOrder.LITTLE_ENDIAN).writeInt(10 + payload.length());
        buf.order(ByteOrder.LITTLE_ENDIAN).writeInt(requestId);
        buf.order(ByteOrder.LITTLE_ENDIAN).writeInt(type);
        buf.writeBytes(payload.getBytes(StandardCharsets.UTF_8));
        buf.writeByte(0);
        buf.writeByte(0);
    }

    private void sendMultiPacketResponse(ChannelHandlerContext ctx, int requestId, String payload) throws IOException {
        int length = payload.length();

        while (length != 0) {
            int truncated = length > 4096 ? 4096 : length;

            ByteBuf buf = ctx.alloc().buffer();
            createResponse(buf, requestId, TYPE_RESPONSE, payload.substring(0, truncated));
            ctx.write(buf);
            payload = payload.substring(truncated);
            length = payload.length();
        }
    }
}
