package net.glowstone.net.codec.play.scoreboard;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;

import io.netty.buffer.ByteBuf;

import net.glowstone.net.message.play.scoreboard.ScoreboardObjectiveMessage;

import java.io.IOException;

public final class ScoreboardObjectiveCodec implements Codec<ScoreboardObjectiveMessage> {

    @Override
    public ScoreboardObjectiveMessage decode(ByteBuf buf) throws IOException {
        throw new UnsupportedOperationException("Cannot decode ScoreboardObjectiveMessage");
    }

    @Override
    public ByteBuf encode(ByteBuf buf, ScoreboardObjectiveMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buf, message.getName());
        buf.writeByte(message.getAction());

        if (message.getAction() == 0 || message.getAction() == 2) {
            ByteBufUtils.writeUTF8(buf, message.getDisplayName());
            ByteBufUtils.writeUTF8(buf, message.getType());
        }

        return buf;
    }
}
