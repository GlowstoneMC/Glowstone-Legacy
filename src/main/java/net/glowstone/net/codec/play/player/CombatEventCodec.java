package net.glowstone.net.codec.play.player;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.net.message.play.player.CombatEventMessage;

import java.io.IOException;

public class CombatEventCodec implements Codec<CombatEventMessage> {

    @Override
    public CombatEventMessage decode(ByteBuf buffer) throws IOException {
        CombatEventMessage combatEventMessage;
        CombatEventMessage.Event event = CombatEventMessage.Event.getAction(buffer.readUnsignedByte());

        if (event == CombatEventMessage.Event.END_COMBAT) {
            int duration = ByteBufUtils.readVarInt(buffer);
            int entityID = buffer.readInt();
            combatEventMessage = new CombatEventMessage(event, duration, entityID);
        } else if (event == CombatEventMessage.Event.ENTITY_DEAD) {
            int playerID = ByteBufUtils.readVarInt(buffer);
            int entityID = buffer.readInt();
            String message = ByteBufUtils.readUTF8(buffer);
            combatEventMessage = new CombatEventMessage(event, playerID, entityID, message);
        } else {
            combatEventMessage = new CombatEventMessage(event);
        }
        return combatEventMessage;
    }

    @Override
    public ByteBuf encode(ByteBuf buf, CombatEventMessage message) throws IOException {
        buf.writeByte(message.getEvent().getId());
        if (message.getEvent() == CombatEventMessage.Event.END_COMBAT) {
            ByteBufUtils.writeVarInt(buf, message.getDuration());
            ByteBufUtils.writeVarInt(buf, message.getEntityID());
        } else if (message.getEvent() == CombatEventMessage.Event.ENTITY_DEAD) {
            ByteBufUtils.writeVarInt(buf, message.getPlayerID());
            ByteBufUtils.writeVarInt(buf, message.getEntityID());
            ByteBufUtils.writeUTF8(buf, message.getMessage());
        }
        return buf;
    }
}
