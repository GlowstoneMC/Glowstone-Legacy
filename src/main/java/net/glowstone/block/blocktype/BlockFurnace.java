package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.block.entity.TEFurnace;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.inventory.ToolType;
import org.bukkit.Material;

public class BlockFurnace extends DefaultBlockType {

    public BlockFurnace() {
        super(
                new BlockContainer() {
                    @Override
                    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
                        return new TEFurnace(chunk.getBlock(cx, cy, cz));
                    }
                },
                new BlockDirectional(),
                new BlockDirectDrops(Material.FURNACE, ToolType.PICKAXE)
        );
    }
}
