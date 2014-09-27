package net.glowstone.network.handler.play.player;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.EventFactory;
import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.entity.AnimateEntityMessage;
import net.glowstone.network.message.play.player.PlayerSwingArmMessage;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;

public final class PlayerSwingArmHandler implements MessageHandler<GlowSession, PlayerSwingArmMessage> {
    @Override
    public void handle(GlowSession session, PlayerSwingArmMessage message) {
        final GlowPlayer player = session.getPlayer();

        Block block;
        try {
            block = player.getTargetBlock(null, 6);
        } catch (IllegalStateException ex) {
            // getTargetBlock failed to find any block at all
            block = null;
        }

        if (block == null || block.isEmpty()) {
            if (EventFactory.onPlayerInteract(player, Action.LEFT_CLICK_AIR).useItemInHand() == Event.Result.DENY)
                return;
            // todo: item interactions with air
        }

        if (!EventFactory.callEvent(new PlayerAnimationEvent(player)).isCancelled()) {
            // play the animation to others
            AnimateEntityMessage toSend = new AnimateEntityMessage(player.getEntityId(), AnimateEntityMessage.OUT_SWING_ARM);
            for (GlowPlayer observer : player.getWorld().getRawPlayers()) {
                if (observer != player && observer.canSee((GlowEntity) player)) {
                    observer.getSession().send(toSend);
                }
            }
        }
    }
}
