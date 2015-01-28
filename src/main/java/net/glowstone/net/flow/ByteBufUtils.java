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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A class containing various utility methods that act on byte buffers.
 */
public class ByteBufUtils {

    private ByteBufUtils() {
    }

    /**
     * Reads an UTF8 string from a byte buffer.
     * @param buf The byte buffer to read from
     * @return The read string
     * @throws java.io.IOException If the reading fails
     */
    public static String readUTF8(ByteBuf buf) throws IOException {
        // Read the string's length
        final int len = readVarInt(buf);
        final byte[] bytes = new byte[len];
        buf.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Writes an UTF8 string to a byte buffer.
     * @param buf The byte buffer to write too
     * @param value The string to write
     * @throws java.io.IOException If the writing fails
     */
    public static void writeUTF8(ByteBuf buf, String value) throws IOException {
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        if (bytes.length >= Short.MAX_VALUE) {
            throw new IOException("Attempt to write a string with a length greater than Short.MAX_VALUE to ByteBuf!");
        }
        // Write the string's length
        writeVarInt(buf, bytes.length);
        buf.writeBytes(bytes);
    }

    /**
     * Reads an integer written into the byte buffer as one of various bit sizes.
     * @param buf The byte buffer to read from
     * @return The read integer
     * @throws java.io.IOException If the reading fails
     */
    public static int readVarInt(ByteBuf buf) throws IOException {
        int out = 0;
        int bytes = 0;
        byte in;
        while (true) {
            in = buf.readByte();
            out |= (in & 0x7F) << (bytes++ * 7);
            if (bytes > 5) {
                throw new IOException("Attempt to read int bigger than allowed for a varint!");
            }
            if ((in & 0x80) != 0x80) {
                break;
            }
        }
        return out;
    }

    /**
     * Writes an integer into the byte buffer using the least possible amount of bits.
     * @param buf The byte buffer to write too
     * @param value The integer value to write
     */
    public static void writeVarInt(ByteBuf buf, int value) {
        byte part;
        while (true) {
            part = (byte) (value & 0x7F);
            value >>>= 7;
            if (value != 0) {
                part |= 0x80;
            }
            buf.writeByte(part);
            if (value == 0) {
                break;
            }
        }
    }

    /**
     * Reads an integer written into the byte buffer as one of various bit sizes.
     * @param buf The byte buffer to read from
     * @return The read integer
     * @throws java.io.IOException If the reading fails
     */
    public static long readVarLong(ByteBuf buf) throws IOException {
        long out = 0;
        int bytes = 0;
        byte in;
        while (true) {
            in = buf.readByte();
            out |= (in & 0x7F) << (bytes++ * 7);
            if (bytes > 10) {
                throw new IOException("Attempt to read long bigger than allowed for a varlong!");
            }
            if ((in & 0x80) != 0x80) {
                break;
            }
        }
        return out;
    }

    /**
     * Writes an integer into the byte buffer using the least possible amount of bits.
     * @param buf The byte buffer to write too
     * @param value The long value to write
     */
    public static void writeVarLong(ByteBuf buf, long value) {
        byte part;
        while (true) {
            part = (byte) (value & 0x7F);
            value >>>= 7;
            if (value != 0) {
                part |= 0x80;
            }
            buf.writeByte(part);
            if (value == 0) {
                break;
            }
        }
    }
}
