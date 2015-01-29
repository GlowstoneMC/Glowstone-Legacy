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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A class used to lookup message codecs.
 */
public class MapCodecLookup implements CodecLookup {
    /**
     * Table mapping message classes to their codecs and opcodes.
     */
    private final ConcurrentMap<Class<? extends Message>, CodecRegistration<?>> messages;

    /**
     * Table mapping opcodes to their codecs.
     */
    private final ConcurrentMap<Integer, Codec> codecs;

    /**
     * Construct a new modifiable MapCodecLookup.
     */
    public MapCodecLookup() {
        messages = new ConcurrentHashMap<>();
        codecs = new ConcurrentHashMap<>();
    }

    public ArrayCodecLookup bake() {
        return new ArrayCodecLookup(messages, codecs);
    }

    /**
     * Binds a codec by adding entries for it to the tables.
     * @param messageClazz The message's class
     * @param codecClazz The codec's class.
     * @param opcode the opcode to register with, or null if the codec should be dynamic
     * @param <M> The type of message
     * @param <C> The type of codec.
     * @return the registration object of the codec
     */
    public <M extends Message, C extends Codec<? super M>> CodecRegistration bind(Class<M> messageClazz, Class<C> codecClazz, int opcode) {
        CodecRegistration<M> reg = find(messageClazz);
        if (reg != null) {
            return reg;
        }
        Codec<?> previous = codecs.get(opcode);
        if (previous != null && previous.getClass() != codecClazz) {
            throw new IllegalArgumentException("Trying to bind an opcode where one already exists. New: " + codecClazz.getSimpleName() + " Old: " + previous.getClass().getSimpleName());
        }
        C codec;
        try {
            codec = codecClazz.newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException("Codec could not be created!", e);
        }
        codecs.put(opcode, codec);
        reg = new CodecRegistration<>(opcode, codec);
        messages.put(messageClazz, reg);
        return reg;
    }

    @Override
    public Codec<?> find(int opcode) {
        return codecs.get(opcode);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends Message> CodecRegistration<M> find(Class<M> clazz) {
        return (CodecRegistration<M>) messages.get(clazz);
    }

}
