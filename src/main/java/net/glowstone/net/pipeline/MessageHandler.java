package net.glowstone.net.pipeline;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import net.glowstone.GlowServer;
import net.glowstone.net.GlowSession;
import net.glowstone.net.flow.Message;
import net.glowstone.net.flow.Session;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Experimental pipeline component, based on flow-net's MessageHandler.
 */
public final class MessageHandler extends SimpleChannelInboundHandler<Message> {

    /**
     * The associated session.
     */
    private final AtomicReference<Session> session = new AtomicReference<>(null);

    /**
     * The server.
     */
    private final GlowServer server;

    /**
     * Creates a new network event handler.
     */
    public MessageHandler(GlowServer server) {
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final Channel channel = ctx.channel();
        Session s = new GlowSession(server, channel);
        if (!session.compareAndSet(null, s)) {
            throw new IllegalStateException("Session may not be set more than once");
        }
        s.onReady();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        session.get().onDisconnect();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message i) {
        session.get().messageReceived(i);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ((GlowSession) session.get()).idle();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        session.get().onInboundThrowable(cause);
    }

}
