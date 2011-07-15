package net.glowstone.block.properties;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Diode;

public class DiodeProperties {
    private DiodeProperties() {}


    public static PlayerInteractEvent doInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK)
            event.setCancelled(true);
        Block block = event.getClickedBlock();
        Diode diode = (Diode)block.getType().getNewData(block.getData());
        if (diode.getDelay() == 4) {
            diode.setDelay(1);
        } else {
            diode.setDelay(diode.getDelay() + 1);
        }
        block.setData(diode.getData());
        return event;
    }

    public static Byte doPlace(Player player, Block block, BlockFace face) {
        Diode diode = (Diode)block.getType().getNewData(block.getData());
        double angle = player.getLocation().getYaw();
        if (angle > 45.5 && angle < 135.4) {
            diode.setFacingDirection(BlockFace.EAST);
        } else if (angle > 135.5 && angle < 225.4) {
            diode.setFacingDirection(BlockFace.SOUTH);
        } else if (angle > 225.5 && angle < 315.4) {
            diode.setFacingDirection(BlockFace.WEST);
        } else {
            diode.setFacingDirection(BlockFace.NORTH);
        }
        return diode.getData();
    }
}
