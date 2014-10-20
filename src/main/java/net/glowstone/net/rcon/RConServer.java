package net.glowstone.net.rcon;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import net.glowstone.GlowServer;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RConServer {
    /**
    * The password used by the rcon server.
    */
    private final String password;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    /**
     * The {@link io.netty.bootstrap.Bootstrap} used by netty to instantiate the RCon server.
     */
    private ServerBootstrap bootstrap = new ServerBootstrap();

    public GlowServer getServer() {
        return server;
    }

    /**
     * Instance of the GlowServer.
     */
    private GlowServer server;

    private Map<Integer, Boolean> verifiedRequests = new ConcurrentHashMap<>();

    public RConServer(GlowServer server, final String password) {
        this.server = server;
        this.password = password;

        final RConServer rconServer = this;

        bootstrap
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        System.out.println("Channel created!");
                        ch.pipeline().
                                addLast(new RConFramingHandler()).
                                addLast(new RConHandler(rconServer, password));
                    }
                });
    }

    /**
     * Bind the server on the specified address.
     * @param address The address.
     * @return Netty channel future for bind operation.
     */
    public ChannelFuture bind(final SocketAddress address) {
        return bootstrap.bind(address);
    }

    /**
     * Shut the RCon server down.
     */
    public void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
