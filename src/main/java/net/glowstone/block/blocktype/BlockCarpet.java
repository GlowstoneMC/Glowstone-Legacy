package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class BlockCarpet extends BlockNeedsAttached {
    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        return super.canPlaceAt(block, against) && block.getRelative(BlockFace.DOWN).getType() != Material.AIR;
    }
}
