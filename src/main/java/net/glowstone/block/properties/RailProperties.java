package net.glowstone.block.properties;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class RailProperties {
    private RailProperties() {}
    
    public static boolean canPlace(Location loc) {
        Material mat = loc.getBlock().getRelative(BlockFace.DOWN).getType();
        if (mat != Material.AIR && mat != Material.RAILS && mat != Material.POWERED_RAIL && mat != Material.DETECTOR_RAIL)
            return false;
        return true;
    }
}
