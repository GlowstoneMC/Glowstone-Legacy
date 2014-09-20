package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.entity.GlowPlayer;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class BlockDaylightDetector extends BlockType {
    
    @Override    
    public boolean blockInteract(GlowPlayer player, GlowBlock block, BlockFace face, Vector clickedLoc) {
        block.setType(Material.DAYLIGHT_DETECTOR_INVERTED);
        return true;
    }
}
