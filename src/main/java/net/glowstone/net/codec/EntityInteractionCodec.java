package net.glowstone.net.codec;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import net.glowstone.msg.EntityInteractionMessage;

public final class EntityInteractionCodec extends MessageCodec<EntityInteractionMessage> {

    public EntityInteractionCodec() {
        super(EntityInteractionMessage.class, 0x07);
    }

    @Override
    public EntityInteractionMessage decode(ChannelBuffer buffer) throws IOException {
        int id = buffer.readInt();
        int target = buffer.readInt();
        boolean punching = buffer.readByte() != 0;
        return new EntityInteractionMessage(id, target, punching);
    }

    @Override
    public ChannelBuffer encode(EntityInteractionMessage message) throws IOException {
        ChannelBuffer buffer = ChannelBuffers.buffer(9);
        buffer.writeInt(message.getId());
        buffer.writeInt(message.getTarget());
        buffer.writeByte(message.isPunching() ? 1 : 0);
        return buffer;
    }

}
