package net.glowstone.block.properties;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Lever;

public class LeverProperties {
    private LeverProperties() {}


    public static PlayerInteractEvent doInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Lever lever = (Lever)block.getType().getNewData(block.getData());
        lever.setPowered(!lever.isPowered());
        block.setData(lever.getData());
        return event;
    }

    public static Byte doPlace(Player player, Block block, BlockFace face) {
        Lever lever = (Lever)block.getType().getNewData(block.getData());
        lever.setFacingDirection(face);
        if (face == BlockFace.DOWN) {
            double angle = player.getLocation().getYaw();
            if (angle > 45.5 && angle < 135.4) {
                lever.setFacingDirection(BlockFace.EAST);
            } else if (angle > 135.5 && angle < 225.4) {
                lever.setFacingDirection(BlockFace.SOUTH);
            } else if (angle > 225.5 && angle < 315.4) {
                lever.setFacingDirection(BlockFace.WEST);
            } else {
                lever.setFacingDirection(BlockFace.NORTH);
            }
        }
        return lever.getData();
    }
}
