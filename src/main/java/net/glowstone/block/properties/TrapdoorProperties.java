package net.glowstone.block.properties; 

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.TrapDoor;

public class TrapdoorProperties {
    private TrapdoorProperties() {}
    
    public static boolean canPlace(Location loc) {
        if (loc.getBlock().getRelative(BlockFace.NORTH).getType() != Material.AIR)
            return true;
        if (loc.getBlock().getRelative(BlockFace.SOUTH).getType() != Material.AIR)
            return true;
        if (loc.getBlock().getRelative(BlockFace.EAST).getType() != Material.AIR)
            return true;
        if (loc.getBlock().getRelative(BlockFace.WEST).getType() != Material.AIR)
            return true;
         return false;
    }

    public static PlayerInteractEvent doInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        TrapDoor trap = (TrapDoor)block.getType().getNewData(block.getData());
        byte data = (byte)(trap.isOpen() ? block.getData() - 0x4 : block.getData() + 0x4);
        block.setData(data);
        return event;
    }

    protected static Byte doPlace(Player player, Block block, BlockFace face) {
        TrapDoor trap = (TrapDoor)block.getType().getNewData(block.getData());
        trap.setFacingDirection(face);
        return trap.getData();
    }
}
