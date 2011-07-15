package net.glowstone.msg.handler;

import net.glowstone.block.BlockProperties;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

        BlockFace face = BlockFace.WEST;
        switch (message.getFace()) {
            case 0:
                face = BlockFace.DOWN;
                break;
            case 1:
                face = BlockFace.UP;
                break;
            case 2:
                face = BlockFace.EAST;
                break;
            case 3:
                face =  BlockFace.WEST;
                break;
            case 4:
                face = BlockFace.NORTH;
                break;
            case 5:
                face = BlockFace.SOUTH;
                break;
        }

        // Notch only allows block clicks for within 6 blocks
        if (block.getLocation().distance(player.getLocation()) > 6.0) {
            EventFactory.onPlayerInteract(player, Action.LEFT_CLICK_AIR, block, face);
            return;
        }


        // Need to have some sort of verification to deal with malicious clients.
        if (message.getState() == DiggingMessage.STATE_START_DIGGING) {
            // Runs the interact events for punching a block
            PlayerInteractEvent interactEvent = EventFactory.onPlayerInteract(player, Action.LEFT_CLICK_BLOCK, block, face);
            if (BlockProperties.get(block.getType()).isInteractable()) {
                interactEvent = BlockProperties.get(block.getType()).doInteract(interactEvent);
                if (interactEvent.useInteractedBlock() == Event.Result.ALLOW) {
                    return;
                }
            }
            BlockDamageEvent event = EventFactory.onBlockDamage(player, block);
            if (!event.isCancelled()) {
                blockBroken = event.getInstaBreak();
            }
            blockBroken = BlockProperties.get(block.getType()).getHardness() == 0F;
        } else if (message.getState() == DiggingMessage.STATE_DONE_DIGGING) {
            BlockBreakEvent event = EventFactory.onBlockBreak(block, player);
            if (!event.isCancelled()) {
                blockBroken = true;
            }
        }

        if (blockBroken) {
            if (block.getType() != Material.AIR) {
                Byte dataDrops = block.getData();
                ItemStack[] drops = BlockProperties.get(block.getType()).getDropsWithData((dataDrops != null) ? dataDrops : -1);
                if (drops != null)
                for (ItemStack stack : Arrays.asList(drops)) {
                    player.getInventory().addItem(stack);
                }
            }
            block.setType(Material.AIR);
        }
    }

}
