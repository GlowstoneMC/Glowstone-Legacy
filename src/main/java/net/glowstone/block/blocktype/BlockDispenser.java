package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.block.entity.TEDispenser;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.inventory.ToolType;

public class BlockDispenser extends DefaultBlockType {

    public BlockDispenser() {
        super(
                new BlockContainer() {
                    @Override
                    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
                        return new TEDispenser(chunk.getBlock(cx, cy, cz));
                    }
                },
                new BlockDirectional(),
                new BlockDropWithoutData(ToolType.PICKAXE)
        );
    }
}
