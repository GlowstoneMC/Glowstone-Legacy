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

/**
 * This class defines a basic structure for any object which manages connections.
 */
public interface ConnectionManager {
    /**
     * Creates a new Session for a {@code Channel}. This session will be used for all API-facing actions.
     * Therefore, this session will most likely be saved by the {@code ConnectionManager} in order to interact with the
     * {@code Session}.
     * @param c the Channel the Session will be using
     * @return the new Session
     */
    Session newSession(Channel c);

    /**
     * Called when a session becomes inactive because the underlying channel has been closed.
     * All references to the Session should be removed, as it will no longer be valid.
     * @param session the Session which will become inactive
     */
    void sessionInactivated(Session session);

    void shutdown();
}
