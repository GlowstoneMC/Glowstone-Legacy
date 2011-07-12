package net.glowstone.block.properties; 

import org.bukkit.Location; 
import org.bukkit.block.BlockFace; 
import org.bukkit.Material;

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
}
