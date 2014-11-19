package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.block.entity.TEBrewingStand;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.inventory.ToolType;
import org.bukkit.Material;

public class BlockBrewingStand extends DefaultBlockType {

    public BlockBrewingStand() {
        super(
                new BlockDirectDrops(Material.BREWING_STAND_ITEM, ToolType.PICKAXE),
                new BlockContainer() {
                    @Override
                    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
                        return new TEBrewingStand(chunk.getBlock(cx, cy, cz));
                    }
                }
        );
    }
}
