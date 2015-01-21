package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.block.BlockFace;

public class BlockCarpet extends DefaultBlockType {
    public BlockCarpet() {
        super(
                new BlockNeedsAttached()
        );
    }

    @Override
    public Boolean canPlaceAt(GlowBlock block, BlockFace against) {
        return super.canPlaceAt(block, against) && !block.getRelative(BlockFace.DOWN).isEmpty();
    }
}
