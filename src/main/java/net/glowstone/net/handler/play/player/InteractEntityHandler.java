package net.glowstone.net.handler.play.player;

import com.flowpowered.networking.MessageHandler;

import net.glowstone.GlowServer;
import net.glowstone.GlowWorld;
import net.glowstone.entity.GlowEntity;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.player.InteractEntityMessage;

public final class InteractEntityHandler implements MessageHandler<GlowSession, InteractEntityMessage> {
    @Override
    public void handle(GlowSession session, InteractEntityMessage message) {
       
    	final GlowPlayer player = session.getPlayer();
        if (player == null)
            return;
        
    	final GlowWorld world = player.getWorld();
        final GlowEntity Entity = world.getEntityManager().getEntity(message.getId());
        
        Entity.entityInteract(player, message);
    }
}
