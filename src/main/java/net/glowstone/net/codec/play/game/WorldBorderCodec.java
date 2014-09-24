package net.glowstone.net.codec.play.game;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;
import net.glowstone.net.message.play.game.WorldBorderMessage;

import java.io.IOException;

public class WorldBorderCodec implements Codec<WorldBorderMessage> {

    @Override
    public WorldBorderMessage decode(ByteBuf buffer) throws IOException {
        WorldBorderMessage worldBorderMessage;
        WorldBorderMessage.Action action = WorldBorderMessage.Action.getAction(ByteBufUtils.readVarInt(buffer));
        switch (action) {
            case SET_SIZE:
                double radius = buffer.readDouble();
                worldBorderMessage = new WorldBorderMessage(action, radius);
                break;
            case LERP_SIZE:
                double oldRadius = buffer.readDouble();
                double newRadius = buffer.readDouble();
                long speed = buffer.readLong();
                worldBorderMessage = new WorldBorderMessage(action, oldRadius, newRadius, speed);
                break;
            case SET_CENTER:
                double x = buffer.readDouble();
                double z = buffer.readDouble();
                worldBorderMessage = new WorldBorderMessage(action, x, z);
                break;
            case INITIALIZE:
                x = buffer.readDouble();
                z = buffer.readDouble();
                oldRadius = buffer.readDouble();
                newRadius = buffer.readDouble();
                speed = ByteBufUtils.readVarLong(buffer);
                int portalTeleportBoundary = ByteBufUtils.readVarInt(buffer);
                int warningTime = ByteBufUtils.readVarInt(buffer);
                int warningBlocks = ByteBufUtils.readVarInt(buffer);
                worldBorderMessage = new WorldBorderMessage(action, x, z, oldRadius, newRadius, speed, portalTeleportBoundary, warningTime, warningBlocks);
                break;
            case SET_WARNING_TIME:
            case SET_WARNING_BLOCKS:
                warningTime = ByteBufUtils.readVarInt(buffer);
                worldBorderMessage = new WorldBorderMessage(action, warningTime);
                break;
            default:
                throw new DecoderException("Invalid Action ID!");
        }
        return worldBorderMessage;
    }

    @Override
    public ByteBuf encode(ByteBuf buf, WorldBorderMessage message) throws IOException {
        ByteBufUtils.writeVarInt(buf, message.getAction().getId());
        switch (message.getAction()) {
            case SET_SIZE:
                buf.writeDouble(message.getRadius());
                break;
            case LERP_SIZE:
                buf.writeDouble(message.getOldRadius());
                buf.writeDouble(message.getNewRadius());
                buf.writeLong(message.getSpeed());
                break;
            case SET_CENTER:
                buf.writeDouble(message.getX());
                buf.writeDouble(message.getZ());
                break;
            case INITIALIZE:
                buf.writeDouble(message.getX());
                buf.writeDouble(message.getZ());
                buf.writeDouble(message.getOldRadius());
                buf.writeDouble(message.getNewRadius());
                ByteBufUtils.writeVarLong(buf, message.getSpeed());
                ByteBufUtils.writeVarInt(buf, message.getPortalTeleportBoundary());
                ByteBufUtils.writeVarInt(buf, message.getWarningTime());
                ByteBufUtils.writeVarInt(buf, message.getWarningBlocks());
                break;
            case SET_WARNING_TIME:
                ByteBufUtils.writeVarInt(buf, message.getWarningTime());
                break;
            case SET_WARNING_BLOCKS:
                ByteBufUtils.writeVarInt(buf, message.getWarningBlocks());
                break;
        }
        return buf;
    }
}
