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

import org.slf4j.Logger;

/**
 * Represents a connection to another engine.
 * <br/>
 * Controls the state, protocol and channels of a connection to another engine.
 */
public interface Session {
    /**
     * Passes a message to a session for processing.
     * @param message message to be processed
     */
    <T extends Message> void messageReceived(T message);

    /**
     * Gets the protocol associated with this session.
     * @return the protocol
     */
    Protocol getProtocol();

    MessageProcessor getProcessor();

    /**
     * Sends a message across the network.
     * @param message The message.
     */
    void send(Message message) throws ChannelClosedException;

    /**
     * Sends any amount of messages to the client.
     * @param messages the messages to send to the client
     */
    void sendAll(Message... messages) throws ChannelClosedException;

    /**
     * Closes the session.
     */
    void disconnect();

    /**
     * Called after the Session has been disconnected, right before the Session is invalidated.
     */
    void onDisconnect();

    /**
     * Called once the Session is ready for messages.
     */
    void onReady();

    /**
     * Called when a throwable is thrown in the pipeline during inbound operations.
     * @param throwable
     */
    void onInboundThrowable(Throwable throwable);

    Logger getLogger();
}
