package net.glowstone.msg.handler;

import net.glowstone.block.BlockProperties;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import net.glowstone.EventFactory;
import net.glowstone.GlowWorld;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.msg.DiggingMessage;
import net.glowstone.net.Session;

import java.util.Arrays;

/**
 * A {@link MessageHandler} which processes digging messages.
 * @author Zhuowei Zhang
 */
public final class DiggingMessageHandler extends MessageHandler<DiggingMessage> {

    @Override
    public void handle(Session session, GlowPlayer player, DiggingMessage message) {
        if (player == null)
            return;

        boolean blockBroken = false;

        GlowWorld world = player.getWorld();

        int x = message.getX();
        int y = message.getY();
        int z = message.getZ();

        Block block = world.getBlockAt(x, y, z);

        // Need to have some sort of verification to deal with malicious clients.
        if (message.getState() == DiggingMessage.STATE_START_DIGGING) {
            BlockDamageEvent event = EventFactory.onBlockDamage(player, block);
            if (!event.isCancelled()) {
                blockBroken = event.getInstaBreak();
            }
        } else if (message.getState() == DiggingMessage.STATE_DONE_DIGGING) {
            BlockBreakEvent event = EventFactory.onBlockBreak(block, player);
            if (!event.isCancelled()) {
                blockBroken = true;
            }
        }

        if (blockBroken) {
            if (block.getType() != Material.AIR) {
                ItemStack[] drops = BlockProperties.get(block.getType()).getDropsWithData(block.getData());
                if (drops != null)
                for (ItemStack stack : Arrays.asList(drops)) {
                    player.getInventory().addItem(stack);
                }
            }
            block.setType(Material.AIR);
        }
    }

}
