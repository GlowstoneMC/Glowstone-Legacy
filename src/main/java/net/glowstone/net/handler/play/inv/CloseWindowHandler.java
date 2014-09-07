package net.glowstone.net.handler.play.inv;

import com.flowpowered.networking.MessageHandler;

import net.glowstone.GlowServer;
import net.glowstone.block.state.GlowChest;
import net.glowstone.block.state.GlowEnderChest;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.net.GlowSession;
import net.glowstone.net.message.play.inv.CloseWindowMessage;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public final class CloseWindowHandler implements MessageHandler<GlowSession, CloseWindowMessage> {
    public void handle(GlowSession session, CloseWindowMessage message) {
        final GlowPlayer player = session.getPlayer();

        // todo: drop items from workbench, enchant inventory, own crafting grid if needed

        player.closeInventory();

        if (player.getItemOnCursor() != null) {
            // player.getWorld().dropItem(player.getEyeLocation(), player.getItemInHand());
            if (player.getGameMode() != GameMode.CREATIVE) {
                player.getInventory().addItem(player.getItemOnCursor());
            }
            player.setItemOnCursor(null);
        }
        
        if (player.GetBindChest() != null){  
        	World world = player.getWorld();
        	Block bl = world.getBlockAt(player.GetBindChest());
        	if (bl != null){
	        	if (bl.getType() == Material.CHEST){
		       	((GlowChest) bl.getState()).setState((byte)0);
		       	((GlowChest) bl.getState()).ChestAnimation((byte)0);
	        	player.SetBindChest(null);
	        	}
	        	else if (bl.getType() == Material.ENDER_CHEST){
	        	((GlowEnderChest) bl.getState()).setState((byte)0);
			    ((GlowEnderChest) bl.getState()).ChestAnimation((byte)0);
		        player.SetBindChest(null);
	        	}
        	}
        }
    }
}
