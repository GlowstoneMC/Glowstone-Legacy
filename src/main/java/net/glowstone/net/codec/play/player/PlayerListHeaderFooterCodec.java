package net.glowstone.net.codec.play.player;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.glowstone.net.message.play.player.PlayerListHeaderFooterMessage;

import java.io.IOException;

public class PlayerListHeaderFooterCodec implements Codec<PlayerListHeaderFooterMessage> {
    
    @Override
    public PlayerListHeaderFooterMessage decode(ByteBuf buffer) throws IOException {
        throw new DecoderException("Cannot decode PlayerListHeaderFooterMessage");
    }

    @Override
    public ByteBuf encode(ByteBuf buf, PlayerListHeaderFooterMessage message) throws IOException {
        ByteBufUtils.writeUTF8(buf, message.getHeader().toJSONString());
        ByteBufUtils.writeUTF8(buf, message.getFooter().toJSONString());
        return buf;
    }
}
