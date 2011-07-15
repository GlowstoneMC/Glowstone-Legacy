package net.glowstone.msg.handler;

import net.glowstone.EventFactory;
import net.glowstone.block.BlockProperties;
import net.glowstone.block.ItemProperties;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
        BlockFace againstFace = BlockFace.WEST;
        switch (message.getDirection()) {
            case 0:
                againstFace = BlockFace.DOWN;
                --y;
                break;
            case 1:
                againstFace = BlockFace.UP;
                ++y;
                break;
            case 2:
                againstFace = BlockFace.EAST;
                --z;
                break;
            case 3:
                againstFace =  BlockFace.WEST;
                ++z;
                break;
            case 4:
                againstFace = BlockFace.NORTH;
                --x;
                break;
            case 5:
                againstFace = BlockFace.SOUTH;
                ++x;
                break;
        }
        Block placed = world.getBlockAt(x, y, z);
        if (placed.getType() != Material.AIR) {
            player.updateInventory();
            return;
        }
        PlayerInteractEvent event = EventFactory.onPlayerInteract(player, Action.RIGHT_CLICK_BLOCK, placedAgainst, againstFace);
        boolean interact = BlockProperties.get(placedAgainst.getType()).isInteractable();
        if (interact)
            event = BlockProperties.get(placedAgainst.getType()).doInteract(event);
        if (!event.isCancelled() && interact) {
            resetPlace(player, placed, placed.getData());
            return;
        }
        if (!canPlace)
            placed.setType(Material.AIR);

        if (player.getItemInHand() != null) if (player.getItemInHand().getTypeId() < 256) {
            doBlockPlace(canPlace, player.getItemInHand().getType(), placed, player, againstFace);
        } else {
            if (ItemProperties.get(player.getItemInHand().getType()).placesBlock())
                doBlockPlace(canPlace, ItemProperties.get(player.getItemInHand().getType()).getPlaceBlock(), placed, player, againstFace);
        }
    }

    private boolean doBlockPlace(boolean canPlace, Material mat, Block block, GlowPlayer player, BlockFace against) {
        if (!BlockProperties.get(mat).isPlaceableAt(block.getLocation()))
            canPlace = false;
        BlockCanBuildEvent canBuildEvent = EventFactory.onBlockCanBuild(block, mat.getId(), canPlace);
        canPlace = canBuildEvent.isBuildable();

        if (block.getType() == Material.AIR) {
            ItemStack stack = player.getItemInHand();
            // This won't work properly (on two-block blocks) until physics is added
            // Unless it is done in BlockProperties in a different way
            // Or here in a different way
            byte oldData = block.getData();
            block.setType(mat);
            Byte data = BlockProperties.get(mat).specialPlace(player, block, against);
            block.setData(data == null ? (byte)stack.getDurability() : data);
            BlockPlaceEvent event = EventFactory.onBlockPlace(block, block.getState(), block, player, canPlace);
            if (event.canBuild() && !event.isCancelled()) {

                stack.setAmount(stack.getAmount() - 1);
                if (stack.getAmount() == 0) {
                    player.setItemInHand(null);
                } else {
                    player.setItemInHand(stack);
                }
                return true;
            } else {
                resetPlace(player, block, oldData);
                return false;
            }
        }
        return false;
    }

    private void resetPlace(GlowPlayer player, Block block, byte oldData) {
        block.setType(Material.AIR);
        block.setData(oldData);
        player.updateInventory();
    }
}
