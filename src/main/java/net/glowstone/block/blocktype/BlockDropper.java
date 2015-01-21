package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.block.entity.TEDropper;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.inventory.ToolType;

public class BlockDropper extends DefaultBlockType {

    public BlockDropper() {
        super(
                new BlockContainer() {
                    @Override
                    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
                        return new TEDropper(chunk.getBlock(cx, cy, cz));
                    }
                },
                new BlockDropWithoutData(ToolType.PICKAXE)
        );
    }
}
