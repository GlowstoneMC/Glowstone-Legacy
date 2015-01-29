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

import java.util.HashMap;
import java.util.Map;

/**
 * Lookup from {@link Message} classes to {@link MessageHandler} instances.
 */
public class HandlerLookupService implements HandlerLookup {

    /**
     * Table from message classes to handler instances.
     */
    private final Map<Class<? extends Message>, MessageHandler<?, ?>> handlers = new HashMap<>();

    /**
     * Bind a message class to a handler class. The handler class should be
     * possible to trivially construct.
     * @param clazz The message class to bind.
     * @param handlerClass The handler class to bind.
     * @throws IllegalArgumentException if an error occurs
     */
    public <M extends Message> void bind(Class<M> clazz, Class<? extends MessageHandler<?, ? super M>> handlerClass) throws IllegalArgumentException {
        try {
            handlers.put(clazz, handlerClass.newInstance());
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("Failed to create handler instance", ex);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends Message> MessageHandler<?, M> getHandler(Class<M> clazz) {
        return (MessageHandler<?, M>) handlers.get(clazz);
    }

}
