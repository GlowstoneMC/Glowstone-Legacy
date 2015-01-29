package net.glowstone.net.protocol;

import net.glowstone.GlowServer;
import net.glowstone.net.flow.*;

import java.util.logging.Level;

public abstract class GlowProtocol implements HandlerLookup {

    private MapCodecLookup wipInbound;
    private MapCodecLookup wipOutbound;

    private ArrayCodecLookup inboundCodecs;
    private ArrayCodecLookup outboundCodecs;

    private final HandlerLookupService handlers;
    private final String name;

    public GlowProtocol(String name) {
        this.name = name;
        wipInbound = new MapCodecLookup();
        wipOutbound = new MapCodecLookup();
        handlers = new HandlerLookupService();
    }

    // creating bindings

    protected final <M extends Message, C extends Codec<? super M>, H extends MessageHandler<?, ? super M>> void inbound(int opcode, Class<M> message, Class<C> codec, Class<H> handler) {
        try {
            wipInbound.bind(message, codec, opcode);
            handlers.bind(message, handler);
        } catch (Exception e) {
            GlowServer.logger.log(Level.SEVERE, "Error registering inbound 0x" + Integer.toHexString(opcode) + " in " + name, e);
        }
    }

    protected final <M extends Message, C extends Codec<? super M>> void outbound(int opcode, Class<M> message, Class<C> codec) {
        try {
            wipOutbound.bind(message, codec, opcode);
        } catch (Exception e) {
            GlowServer.logger.log(Level.SEVERE, "Error registering outbound 0x" + Integer.toHexString(opcode) + " in " + name, e);
        }
    }

    protected final void done() {
        inboundCodecs = wipInbound.bake();
        outboundCodecs = wipOutbound.bake();
        wipInbound = wipOutbound = null;
    }

    // accessing bindings

    public final <M extends Message> CodecRegistration<M> getOutboundCodec(Class<M> clazz) {
        CodecRegistration<M> reg = outboundCodecs.find(clazz);
        if (reg == null) {
            GlowServer.logger.warning("No codec to write: " + clazz.getSimpleName() + " in " + name);
        }
        return reg;
    }

    public final Codec<?> getInboundCodec(int opcode) {
        return inboundCodecs.find(opcode);
    }

    @Override
    public final <M extends Message> MessageHandler<?, M> getHandler(Class<M> clazz) {
        MessageHandler<?, M> handler = handlers.getHandler(clazz);
        if (handler == null) {
            GlowServer.logger.warning("No message handler for: " + clazz.getSimpleName() + " in " + name);
        }
        return handler;
    }
}
