package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.block.entity.TEChest;
import net.glowstone.block.entity.TileEntity;

public class BlockChest extends DefaultBlockType {

    public BlockChest() {
        super(
                new BlockContainer() {
                    @Override
                    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
                        return new TEChest(chunk.getBlock(cx, cy, cz));
                    }
                },
                new BlockDirectional()
        );
    }
}
