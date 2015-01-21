package net.glowstone.block.blocktype;

import net.glowstone.EventFactory;
import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.PortalType;
import org.bukkit.World.Environment;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.EnderPortalFrame;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class BlockEnderPortalFrame extends DefaultBlockType {
    private static final BlockFace[] DIRECTION = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};

    public BlockEnderPortalFrame() {
        super(new BlockDirectional());
    }

    @Override
    public Boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        BlockState state = block.getState();
        MaterialData data = state.getData();

        if (!(data instanceof EnderPortalFrame)) {
            warnMaterialData(EnderPortalFrame.class, data);
            return false;
        }

        EnderPortalFrame portalFrame = (EnderPortalFrame) data;

        ItemStack item = player.getItemInHand();
        if (item != null && item.getType() == Material.EYE_OF_ENDER) {
            if (portalFrame.hasEye()) {
                return true;
            }
            if (player.getGameMode() != GameMode.CREATIVE) {
                item.setAmount(item.getAmount() - 1);
            }

            portalFrame.setEye(true);
            state.update();
            if (block.getWorld().getEnvironment() != Environment.THE_END) {
                searchForCompletedPortal(player, block);
            }
            return true;
        }
        return false;
    }

    /**
     * Checks for a completed portal at all relevant positions.
     */
    private void searchForCompletedPortal(GlowPlayer player, GlowBlock changed) {
        for (int i = 0; i < 4; i++) {
            for (int j = -1; j <= 1; j++) {
                GlowBlock center = changed.getRelative(DIRECTION[i], 2).getRelative(DIRECTION[(i + 1) % 4], j);
                if (isCompletedPortal(center)) {
                    createPortal(player, center);
                    return;
                }
            }
        }
    }

    /**
     * Check whether there is a completed portal with the specified center.
     */
    private boolean isCompletedPortal(GlowBlock center) {
        for (int i = 0; i < 4; i++) {
            for (int j = -1; j <= 1; j++) {
                GlowBlock block = center.getRelative(DIRECTION[i], 2).getRelative(DIRECTION[(i + 1) % 4], j);
                if (block.getType() != Material.ENDER_PORTAL_FRAME || (block.getData() & 0x4) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Spawn the portal and call the {@link EntityCreatePortalEvent}.
     */
    private void createPortal(GlowPlayer player, GlowBlock center) {
        List<BlockState> blocks = new ArrayList<>(9);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                BlockState state = center.getRelative(i, 0, j).getState();
                state.setType(Material.ENDER_PORTAL);
                blocks.add(state);
            }
        }
        if (!EventFactory.callEvent(new EntityCreatePortalEvent(player, blocks, PortalType.ENDER)).isCancelled()) {
            for (BlockState state : blocks) {
                state.update(true);
            }
        }
    }
}
