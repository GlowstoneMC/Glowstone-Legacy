package net.glowstone.net.handler.play.inv;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.EventFactory;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.inv.HeldItemMessage;
import org.bukkit.event.player.PlayerItemHeldEvent;

public final class HeldItemHandler implements MessageHandler<GlowSession, HeldItemMessage> {
    @Override
    public void handle(GlowSession session, HeldItemMessage message) {
        final int slot = message.getSlot();
        if (slot < 0 || slot > 8) // sanity check
            return;
        GlowPlayer player = session.getPlayer();

        PlayerItemHeldEvent event = new PlayerItemHeldEvent(player, player.getInventory().getHeldItemSlot(), slot);
        EventFactory.callEvent(event);

        if (!event.isCancelled()) {
            session.getPlayer().getInventory().setRawHeldItemSlot(slot);
        } else {
            // This sends a packet to the player telling them to switch their held slot
            session.getPlayer().getInventory().setHeldItemSlot(player.getInventory().getHeldItemSlot());
        }
    }
}
