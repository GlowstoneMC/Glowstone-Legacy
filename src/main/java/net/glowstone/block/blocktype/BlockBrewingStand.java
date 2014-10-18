package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.entity.TEBrewingStand;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.inventory.MaterialMatcher;
import net.glowstone.inventory.ToolType;

public class BlockBrewingStand extends BlockContainer {

    @Override
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new TEBrewingStand(chunk.getBlock(cx, cy, cz));
    }

    @Override
    protected MaterialMatcher getNeededMiningTool(GlowBlock block) {
        return ToolType.PICKAXE;
    }
}
