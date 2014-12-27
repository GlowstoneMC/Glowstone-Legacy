package net.glowstone.net.handler.play.inv;

import com.flowpowered.networking.MessageHandler;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.inv.CloseWindowMessage;

public final class CloseWindowHandler implements MessageHandler<GlowSession, CloseWindowMessage> {
    @Override
    public void handle(GlowSession session, CloseWindowMessage message) {
        final GlowPlayer player = session.getPlayer();

        // todo: drop items from workbench, enchant inventory, own crafting grid if needed

        //Drop item on cursor, set it to null afterwards
        player.closeInventory();
    }
}
