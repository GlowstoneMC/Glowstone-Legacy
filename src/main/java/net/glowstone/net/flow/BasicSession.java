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
import io.netty.channel.ChannelOption;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Random;

/**
 * A basic implementation of a {@link com.flowpowered.networking.session.Session} which handles and sends messages instantly.
 */
public class BasicSession implements Session {
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
     * The protocol for this session
     */
    private AbstractProtocol protocol;

    /**
     * Creates a new session.
     * @param channel The channel associated with this session.
     * @param bootstrapProtocol the protocol
     */
    public BasicSession(Channel channel, AbstractProtocol bootstrapProtocol) {
        this.channel = channel;
        this.protocol = bootstrapProtocol;
    }

    @SuppressWarnings("unchecked")
    private void handleMessage(Message message) {
        Class<Message> messageClass = (Class<Message>) message.getClass();
        MessageHandler handler = (MessageHandler) protocol.getMessageHandle(messageClass);
        if (handler != null) {
            try {
                handler.handle(this, message);
            } catch (Throwable t) {
                onHandlerThrowable(message, handler, t);
            }
        }
    }

    public ChannelFuture sendWithFuture(Message message) throws ChannelClosedException {
        if (!channel.isActive()) {
            throw new ChannelClosedException("Trying to send a message when a session is inactive!");
        }
        return channel.writeAndFlush(message).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.cause() != null) {
                    onOutboundThrowable(future.cause());
                }
            }
        });
    }

    @Override
    public void send(Message message) throws ChannelClosedException {
        sendWithFuture(message);
    }

    @Override
    public void sendAll(Message... messages) throws ChannelClosedException {
        for (Message msg : messages) {
            send(msg);
        }
    }

    /**
     * Returns the address of this session.
     * @return The remote address.
     */
    public InetSocketAddress getAddress() {
        SocketAddress addr = channel.remoteAddress();
        if (!(addr instanceof InetSocketAddress)) {
            return null;
        }

        return (InetSocketAddress) addr;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [address=" + channel.remoteAddress() + "]";
    }

    /**
     * Adds a message to the unprocessed queue.
     * @param message The message.
     */
    @Override
    public void messageReceived(Message message) {
        handleMessage(message);
    }

    public String getSessionId() {
        return sessionId;
    }

    @Override
    public AbstractProtocol getProtocol() {
        return this.protocol;
    }

    protected void setProtocol(AbstractProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public MessageProcessor getProcessor() {
        return null;
    }

    /**
     * True if this session is open and connected. If the session is closed, errors will be thrown if messages are attempted to be sent.
     * @return is active
     */
    public boolean isActive() {
        return channel.isActive();
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void disconnect() {
        channel.close();
    }

    @Override
    public void onDisconnect() {
    }

    @Override
    public void onReady() {
    }

    @Override
    public void onInboundThrowable(Throwable throwable) {
    }

    public void onOutboundThrowable(Throwable throwable) {
    }

    /**
     * Called when an exception occurs during session handling
     * @param message the message handler threw an exception on
     * @param handle handler that threw the an exception handling the message
     * @param throwable the throwable
     */
    public void onHandlerThrowable(Message message, MessageHandler<?, ?> handle, Throwable throwable) {
    }

    public <T> void setOption(ChannelOption<T> option, T value) {
        channel.config().setOption(option, value);
    }

    @Override
    public Logger getLogger() {
        return protocol.getLogger();
    }
}
