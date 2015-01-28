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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * {@code MessageProcessor} can be used in a {@link PreprocessReplayingDecoder} or {@link ProcessingEncoder} to define
 * how a {@code ByteBuf} should be processed prior to decode or after encode.
 */
public interface MessageProcessor {
    /**
     * Adds the data contained in the given channel buffer to the processor and returns the output channel buffer. The method may be called from multiple threads.<br>
     * This is called after {@code Codec.encode}, but before the message is sent.<br>
     * {@code input.release} should NOT be called; it is done externally.<br>
     * {@code buffer.release} should NOT be called; it is done externally.<br>
     * @param ctx the channel handler context
     * @param input the buffer containing the input data
     * @param buffer the buffer to add the data to; will be dynamically-sized
     * @return the processed outbound ByteBuf
     */
    ByteBuf processOutbound(ChannelHandlerContext ctx, ByteBuf input, ByteBuf buffer);

    /**
     * Adds the data contained in the given channel buffer to the processor and returns the output channel buffer. The method may be called from multiple threads.<br>
     * This should read as much data from {@code input} as possible. It does not replay.<br>
     * This is called after the message arrives, but before {@code Codec.decode} is called.<br>
     * {@code input.release} should NOT be called; it is done externally.<br>
     * {@code buffer.release} should NOT be called; it is done externally.<br>
     * @param ctx the channel handler context
     * @param input the buffer containing the input data
     * @param buffer the buffer to add the data to; will be dynamically-sized
     * @return the processed inbound buffer
     */
    ByteBuf processInbound(ChannelHandlerContext ctx, ByteBuf input, ByteBuf buffer);
}
