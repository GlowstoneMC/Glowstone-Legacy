package net.glowstone.net.codec.play.game;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.game.TitleMessage;

import java.io.IOException;

public class TitleCodec implements Codec<TitleMessage> {

    @Override
    public TitleMessage decode(ByteBuf buffer) throws IOException {
        TitleMessage titleMessage;
        TitleMessage.Action action = TitleMessage.Action.getAction(ByteBufUtils.readVarInt(buffer));
        switch (action) {
            case TITLE:
            case SUBTITLE:
                String text = ByteBufUtils.readUTF8(buffer);
                titleMessage = new TitleMessage(action, text);
                break;
            case TIMES:
                int fadeIn = buffer.readInt();
                int stay = buffer.readInt();
                int fadeOut = buffer.readInt();
                titleMessage = new TitleMessage(action, fadeIn, stay, fadeOut);
                break;
            default:
                titleMessage = new TitleMessage(action);
                break;
        }
        return titleMessage;
    }

    @Override
    public ByteBuf encode(ByteBuf buf, TitleMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buf, message.getAction().getId());
        switch (message.getAction()) {
            case TITLE:
            case SUBTITLE:
                ByteBufUtils.writeUTF8(buf, message.getText());
                break;
            case TIMES:
                buf.writeInt(message.getFadeIn());
                buf.writeInt(message.getStay());
                buf.writeInt(message.getFadeOut());
                break;
        }
        return buf;
    }
}
