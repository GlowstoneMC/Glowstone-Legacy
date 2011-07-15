package net.glowstone.block.properties;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Door;

public class DoorProperties {
    private DoorProperties() {}


    public static PlayerInteractEvent doInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Block second = block.getRelative(BlockFace.UP);
        second = second.getType() != Material.WOOD_DOOR && second.getType() !=Material.IRON_DOOR_BLOCK ? block.getRelative(BlockFace.DOWN) : second;
        Door door = (Door)block.getType().getNewData(block.getData());
        Door secondDoor = (Door)block.getType().getNewData(second.getData());
        door.setOpen(!door.isOpen());
        secondDoor.setOpen(!secondDoor.isOpen());
        block.setData(door.getData());
        second.setData(secondDoor.getData());
        return event;
    }

    public static Byte doPlace(Player player, Block block, BlockFace face) {
        Block second = block.getRelative(BlockFace.UP);
        if (second.getType() != Material.AIR) {
            block.setType(Material.AIR);
            return null;
        }
        second.setType(block.getType());
        Door door = (Door)block.getType().getNewData(block.getData());
        Door secondDoor = (Door)second.getType().getNewData(block.getData());
        secondDoor.setTopHalf(true);
        double angle = player.getLocation().getYaw();
        if (angle > 45.5 && angle < 135.4) {
            door.setFacingDirection(BlockFace.EAST);
            secondDoor.setFacingDirection(BlockFace.EAST);
        } else if (angle > 135.5 && angle < 225.4) {
            door.setFacingDirection(BlockFace.SOUTH);
            secondDoor.setFacingDirection(BlockFace.SOUTH);
        } else if (angle > 225.5 && angle < 315.4) {
            door.setFacingDirection(BlockFace.WEST);
            secondDoor.setFacingDirection(BlockFace.WEST);
        } else {
            door.setFacingDirection(BlockFace.NORTH);
            secondDoor.setFacingDirection(BlockFace.NORTH);
        }
        second.setData(secondDoor.getData());
        return door.getData();
    }
}
