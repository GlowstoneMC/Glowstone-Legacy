package net.glowstone.msg.handler;

import net.glowstone.EventFactory;
import net.glowstone.block.BlockProperties;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.msg.BlockPlacementMessage;
import net.glowstone.net.Session;
import net.glowstone.GlowWorld;

/**
 * A {@link MessageHandler} which processes placing messages.
 * @author Zhuowei Zhang
 */
public final class BlockPlacementMessageHandler extends MessageHandler<BlockPlacementMessage> {

    @Override
    public void handle(Session session, GlowPlayer player, BlockPlacementMessage message) {
        if (player == null)
            return;
        boolean canPlace = true;

        GlowWorld world = player.getWorld();

        int x = message.getX();
        int z = message.getZ();
        int y = message.getY();
        Block placedAgainst = world.getBlockAt(x, y, z);
        if (BlockProperties.get(placedAgainst.getType()).isInteractable()) {
            canPlace = false;
        }
        switch (message.getDirection()) {
            case 0:
                --y; break;
            case 1:
                ++y; break;
            case 2:
                --z; break;
            case 3:
                ++z; break;
            case 4:
                --x; break;
            case 5:
                ++x; break;
        }
        Block placed = world.getBlockAt(x, y, z);
        if (!canPlace)
            placed.setType(Material.AIR);
         
        if (player.getItemInHand() != null && player.getItemInHand().getTypeId() < 256) {
            if (!BlockProperties.get(player.getItemInHand().getType()).isPlaceableAt(placed.getLocation()))
                canPlace = false;
            if (placed.getType() == Material.AIR) {
                placed.setType(player.getItemInHand().getType());
                placed.setData((byte) player.getItemInHand().getDurability());
                BlockPlaceEvent event = EventFactory.onBlockPlace(placed, placed.getState(), placedAgainst, player, canPlace);
                if (event.canBuild() && !event.isCancelled()) {
                    ItemStack stack = player.getItemInHand();
                    stack.setAmount(stack.getAmount() - 1);
                    if (stack.getAmount() == 0) {
                        player.setItemInHand(null);
                    } else {
                        player.setItemInHand(stack);
                    }
                } else {
                    placed.setType(Material.AIR);
                }
            }
        }
    }

}
