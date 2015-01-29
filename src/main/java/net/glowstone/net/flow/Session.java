/*
 * This file is part of Flow Networking, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <https://spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.glowstone.net.flow;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Random;

/**
 * A basic implementation of a session which handles and sends messages instantly.
 */
public class Session {
    /**
     * The Random used for sessionIds.
     */
    private static final Random random = new Random();

    /**
     * The channel associated with this session.
     */
    private final Channel channel;

    /**
     * The random long used for client-server handshake
     */
    private final String sessionId = Long.toString(random.nextLong(), 16).trim();

    /**
     * Creates a new session.
     * @param channel The channel associated with this session.
     */
    public Session(Channel channel) {
        this.channel = channel;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Base accessors

    /**
     * Get the {@link Channel} associated with this session.
     * @return The channel.
     */
    public final Channel getChannel() {
        return channel;
    }

    /**
     * Get the remote address of this session.
     * @return The remote address.
     */
    public InetSocketAddress getAddress() {
        SocketAddress addr = channel.remoteAddress();
        if (!(addr instanceof InetSocketAddress)) {
            return null;
        }

        return (InetSocketAddress) addr;
    }

    /**
     * Get the randomly-generated session id string for this session.
     * @return The session id.
     */
    public final String getSessionId() {
        return sessionId;
    }

    /**
     * Check if the session's channel is currently active.
     * @return true if the channel is active.
     * @see Channel#isActive()
     */
    public final boolean isActive() {
        return channel.isActive();
    }

    @Override
    public String toString() {
        return getClass().getName() + " [address=" + channel.remoteAddress() + "]";
    }

    ////////////////////////////////////////////////////////////////////////////
    // Actions

    /**
     * Send a message through the pipeline.
     * @param message The message to send.
     */
    public final void send(Message message) {
        sendWithFuture(message);
    }

    /**
     * Send a message through the pipeline, returning a {@link ChannelFuture}
     * representing its completion. If the channel has been closed, the message
     * is not sent and null is returned.
     * @param message The message to send.
     * @return The ChannelFuture, or null.
     */
    public ChannelFuture sendWithFuture(Message message) {
        if (!channel.isActive()) {
            return null;
        }
        return channel.writeAndFlush(message).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                Throwable cause = future.cause();
                if (cause != null) {
                    onOutboundThrowable(cause);
                }
            }
        });
    }

    /**
     * Call the appropriate handler for a message.
     * @param message The message to handle.
     */
    @SuppressWarnings("unchecked")
    protected final void handleMessage(Message message) {
        Class<? extends Message> messageClass = message.getClass();
        MessageHandler handler = (MessageHandler) getHandlerLookup().getHandler(messageClass);
        if (handler != null) {
            try {
                handler.handle(this, message);
            } catch (Throwable t) {
                onHandlerThrowable(message, handler, t);
            }
        }
    }

    /**
     * Get the {@link HandlerLookup} to use with {@link #handleMessage(Message)}.
     * By default, a HandlerLookup which never finds a handler is returned.
     * @return The HandlerLookup.
     */
    protected HandlerLookup getHandlerLookup() {
        return HandlerLookup.NULL;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Callbacks

    /**
     * Called once the Session is ready for messages.
     */
    public void onReady() {
    }

    /**
     * Called when a message has been received.
     * @param message the message
     */
    public void onMessageReceived(Message message) {
        handleMessage(message);
    }

    /**
     * Called when the channel has become inactive.
     */
    public void onDisconnect() {
    }

    /**
     * Called when an error occurs on the inbound pipeline.
     * @param throwable the throwable
     */
    public void onInboundThrowable(Throwable throwable) {
    }

    /**
     * Called when an error occurs on the outbound pipeline.
     * @param throwable the throwable
     */
    public void onOutboundThrowable(Throwable throwable) {
    }

    /**
     * Called when an error occurs in a message handler.
     * @param message the message that the handler threw an exception on
     * @param handler the handler that threw the exception
     * @param throwable the throwable
     */
    public void onHandlerThrowable(Message message, MessageHandler<?, ?> handler, Throwable throwable) {
    }

}
