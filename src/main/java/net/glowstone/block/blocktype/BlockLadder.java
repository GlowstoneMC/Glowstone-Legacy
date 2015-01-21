package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import org.bukkit.block.BlockFace;

public class BlockLadder extends DefaultBlockType {

    public BlockLadder() {
        super(
                new BlockClimbable() {
                    @Override
                    public Boolean canPlaceAt(GlowBlock block, BlockFace against) {
                        return super.canPlaceAt(block, against) ||
                                isTargetOccluding(block, BlockFace.SOUTH) ||
                                isTargetOccluding(block, BlockFace.WEST) ||
                                isTargetOccluding(block, BlockFace.NORTH) ||
                                isTargetOccluding(block, BlockFace.EAST);
                    }
                },
                new BlockAttachable(true)
        );
    }
}
