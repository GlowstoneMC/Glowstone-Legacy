package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.block.entity.TEHopper;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.inventory.ToolType;

public class BlockHopper extends DefaultBlockType {

    public BlockHopper() {
        super(
                new BlockDirectional(),
                new BlockContainer() {
                    @Override
                    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
                        return new TEHopper(chunk.getBlock(cx, cy, cz));
                    }
                },
                new BlockDropWithoutData(ToolType.PICKAXE)
        );
    }
}
