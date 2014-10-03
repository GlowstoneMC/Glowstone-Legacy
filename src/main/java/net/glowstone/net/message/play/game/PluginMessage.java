package net.glowstone.net.message.play.game;

import com.flowpowered.networking.Message;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.glowstone.GlowServer;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

public final class PluginMessage implements Message {

    private final String channel;
    private final byte[] data;

    public PluginMessage(String channel, byte[] data) {
        this.channel = channel;
        this.data = data;
    }

    public static PluginMessage fromString(String channel, String text) {
        ByteBuf buf = Unpooled.buffer(5 + text.length());
        try {
            ByteBufUtils.writeUTF8(buf, text);
        } catch (IOException e) {
            GlowServer.logger.log(Level.WARNING, "Error converting to PluginMessage: \"" + channel + "\", \"" + text + "\"", e);
        }
        return new PluginMessage(channel, buf.array());
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PluginMessage that = (PluginMessage) o;

        if (channel != null ? !channel.equals(that.channel) : that.channel != null) return false;
        if (!Arrays.equals(data, that.data)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = channel != null ? channel.hashCode() : 0;
        result = 31 * result + (data != null ? Arrays.hashCode(data) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PluginMessage{" +
                "channel='" + channel + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

}

