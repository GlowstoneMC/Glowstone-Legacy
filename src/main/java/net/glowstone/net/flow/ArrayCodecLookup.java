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

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Set;

/**
 * Array-based codec lookup service.
 */
public class ArrayCodecLookup implements CodecLookup {
    /**
     * Table mapping message classes to their codecs and opcodes.
     */
    private final ImmutableMap<Class<? extends Message>, CodecRegistration<?>> messages;

    /**
     * Table mapping opcodes to their codecs.
     */
    private final Codec[] codecs;

    /**
     * The opcode of codecs[0].
     */
    private final int minOpcode;

    /**
     * Create a new ArrayCodecLookup with the given message and codec maps.
     * @param messages Table from message classes to their codecs and opcodes.
     * @param codecMap Table from opcodes to their codecs.
     */
    public ArrayCodecLookup(Map<Class<? extends Message>, CodecRegistration<?>> messages, Map<Integer, Codec> codecMap) {
        this.messages = ImmutableMap.copyOf(messages);

        if (codecMap.isEmpty()) {
            minOpcode = 0;
            codecs = new Codec[0];
            return;
        }

        // determine bounds of
        int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
        for (Integer key : codecMap.keySet()) {
            min = Math.min(key, min);
            max = Math.max(key, max);
        }

        minOpcode = min;
        codecs = new Codec[max - min + 1];
        for (Map.Entry<Integer, Codec> entry : codecMap.entrySet()) {
            codecs[entry.getKey() - min] = entry.getValue();
        }
    }

    @Override
    public Codec<?> find(int opcode) {
        int offset = opcode - minOpcode;
        if (offset < 0 || offset >= codecs.length) {
            return null;
        }
        return codecs[opcode];
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M extends Message> CodecRegistration<M> find(Class<M> clazz) {
        return (CodecRegistration<M>) messages.get(clazz);
    }

    public Set<Class<? extends Message>> getMessageClasses() {
        return messages.keySet();
    }

}
