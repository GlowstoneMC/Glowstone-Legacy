package net.glowstone.net.query;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.plugin.Plugin;

import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import static io.netty.util.CharsetUtil.UTF_8;

/**
 * Class for handling UPD packets according to the minecraft server query protocol.
 * @see QueryServer
 * @see <a href="http://wiki.vg/Query">Protocol Specifications</a>
 */
public class QueryHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final byte ACTION_HANDSHAKE = 9;
    private static final byte ACTION_STATS = 0;

    /**
     * The {@link QueryServer} this handler belongs to.
     */
    private QueryServer queryServer;

    public QueryHandler(QueryServer queryServer) {
        this.queryServer = queryServer;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        ByteBuf buf = msg.content();
        if (buf.readableBytes() < 7) {
            return;
        }

        int magic = buf.readUnsignedShort();
        byte type = buf.readByte();
        int sessionId = buf.readInt();

        if (magic != 0xFEFD) {
            return;
        }

        if (type == ACTION_HANDSHAKE) {
            handleHandshake(ctx, msg, sessionId);
        }
        if (type == ACTION_STATS) {
            if (buf.readableBytes() < 4) {
                return;
            }
            int token = buf.readInt();
            if (queryServer.verifyChallengeToken(msg.sender(), token)) {
                if (buf.readableBytes() == 4) {
                    handleFullStats(ctx, msg, sessionId);
                } else {
                    handleBasicStats(ctx, msg, sessionId);
                }
            }
        }
    }

    private void handleHandshake(ChannelHandlerContext ctx, DatagramPacket packet, int sessionId) {
        int challengeToken = queryServer.generateChallengeToken(packet.sender());
        ByteBuf out = ctx.alloc().buffer();
        out.writeByte(ACTION_HANDSHAKE);
        out.writeInt(sessionId);
        out.writeBytes(String.valueOf(challengeToken).getBytes(UTF_8));
        out.writeByte(0);
        ctx.write(new DatagramPacket(out, packet.sender()));
    }

    private void handleBasicStats(ChannelHandlerContext ctx, DatagramPacket packet, int sessionId) {
        GlowServer server = queryServer.getServer();
        String motd = server.getMotd();
        String gametype = "SMP";
        String world = server.getWorlds().get(0).getName();
        String numPlayers = String.valueOf(server.getOnlinePlayers().size());
        String maxPlayers = String.valueOf(server.getMaxPlayers());
        int port = server.getPort();
        String ip = server.getIp();
        if (ip.length() == 0) {
            ip = "127.0.0.1";
        }

        ByteBuf buf = ctx.alloc().buffer();
        buf.writeByte(ACTION_STATS);
        buf.writeInt(sessionId);
        buf.writeBytes(motd.getBytes(UTF_8)).writeByte(0);
        buf.writeBytes(gametype.getBytes(UTF_8)).writeByte(0);
        buf.writeBytes(world.getBytes(UTF_8)).writeByte(0);
        buf.writeBytes(numPlayers.getBytes(UTF_8)).writeByte(0);
        buf.writeBytes(maxPlayers.getBytes(UTF_8)).writeByte(0);
        buf.order(ByteOrder.LITTLE_ENDIAN).writeShort(port);
        buf.writeBytes(ip.getBytes(UTF_8)).writeByte(0);
        ctx.write(new DatagramPacket(buf, packet.sender()));
    }

    private void handleFullStats(ChannelHandlerContext ctx, DatagramPacket packet, int sessionId) {
        GlowServer server = queryServer.getServer();
        String ip = server.getIp();
        if (ip.length() == 0) {
            ip = "127.0.0.1";
        }
        StringBuilder plugins = new StringBuilder("Glowstone on Bukkit ").append(server.getBukkitVersion());
        char delim = ':';
        for (Plugin plugin : server.getPluginManager().getPlugins()) {
            plugins.append(delim).append(' ').append(plugin.getDescription().getFullName());
            delim = ';';
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("hostname", server.getMotd());
        data.put("gametype", "SMP");
        data.put("game_id", "MINECRAFT");
        data.put("version", GlowServer.GAME_VERSION);
        data.put("plugins", plugins);
        data.put("map", server.getWorlds().get(0).getName());
        data.put("numplayers", server.getOnlinePlayers().size());
        data.put("maxplayers", server.getMaxPlayers());
        data.put("hostport", server.getPort());
        data.put("hostip", ip);

        ByteBuf buf = ctx.alloc().buffer();
        buf.writeByte(ACTION_STATS);
        buf.writeInt(sessionId);
        buf.writeBytes(new byte[]{0x73, 0x70, 0x6C, 0x69, 0x74, 0x6E, 0x75, 0x6D, 0x00, (byte) 0x80, 0x00});
        for (Entry<String, Object> e : data.entrySet()) {
            buf.writeBytes(e.getKey().getBytes(UTF_8)).writeByte(0);
            buf.writeBytes(String.valueOf(e.getValue()).getBytes(UTF_8)).writeByte(0);
        }
        buf.writeByte(0);
        buf.writeBytes(new byte[]{0x01, 0x70, 0x6C, 0x61, 0x79, 0x65, 0x72, 0x5F, 0x00, 0x00});
        for (GlowPlayer player : server.getOnlinePlayers()) {
            buf.writeBytes(player.getName().getBytes(UTF_8)).writeByte(0);
        }
        buf.writeByte(0);
        ctx.write(new DatagramPacket(buf, packet.sender()));
    }
}
