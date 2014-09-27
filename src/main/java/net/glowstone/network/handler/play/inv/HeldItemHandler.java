package net.glowstone.network.handler.play.inv;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.network.GlowSession;
import net.glowstone.network.message.play.inv.HeldItemMessage;

public final class HeldItemHandler implements MessageHandler<GlowSession, HeldItemMessage> {
    @Override
    public void handle(GlowSession session, HeldItemMessage message) {
        final int slot = message.getSlot();
        if (slot < 0 || slot > 8) // sanity check
            return;
        session.getPlayer().getInventory().setRawHeldItemSlot(slot);
    }
}
