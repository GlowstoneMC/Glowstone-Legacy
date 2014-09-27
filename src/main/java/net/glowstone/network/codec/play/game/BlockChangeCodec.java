package net.glowstone.network.codec.play.game;

import com.flowpowered.networking.Codec;
import com.flowpowered.networking.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.glowstone.network.GlowBufUtils;
import net.glowstone.network.message.play.game.BlockChangeMessage;
import org.bukkit.util.BlockVector;

import java.io.IOException;

public final class BlockChangeCodec implements Codec<BlockChangeMessage> {
    @Override
    public BlockChangeMessage decode(ByteBuf buffer) throws IOException {
        BlockVector pos = GlowBufUtils.readBlockPosition(buffer);
        int type = ByteBufUtils.readVarInt(buffer);
        return new BlockChangeMessage(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ(), type);
    }

    @Override
    public ByteBuf encode(ByteBuf buf, BlockChangeMessage message) throws IOException {
        GlowBufUtils.writeBlockPosition(buf, message.getX(), message.getY(), message.getZ());
        ByteBufUtils.writeVarInt(buf, message.getType());
        return buf;
    }
}
