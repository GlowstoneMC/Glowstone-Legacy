package net.glowstone.msg.handler;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowSign;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.msg.UpdateSignMessage;
import net.glowstone.net.Session;

public final class UpdateSignMessageHandler extends MessageHandler<UpdateSignMessage> {

    @Override
    public void handle(Session session, GlowPlayer player, UpdateSignMessage message) {
        GlowBlock block = player.getWorld().getBlockAt(message.getX(), message.getY(), message.getZ());
        GlowSign sign = new GlowSign(message.getMessage(), block);
        sign.setLines(message.getMessage());
        block.getChunk().addBlockState(block, sign);
        sign.update();

    }
}
