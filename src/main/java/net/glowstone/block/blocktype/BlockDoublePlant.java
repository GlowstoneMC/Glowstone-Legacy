package net.glowstone.block.blocktype;

import org.bukkit.block.BlockFace;
import org.bukkit.material.MaterialData;

import net.glowstone.block.GlowBlock;

public class BlockDoublePlant extends BlockType implements IBlockGrowable {

    @Override
    public void fertilize(GlowBlock block) {
        GlowBlock b = block;
        MaterialData data = block.getState().getData();
        if (data.getData() == 8) { // above block
            b = b.getRelative(BlockFace.DOWN);
            data = b.getState().getData();
        }

        if (data.getData() == 0 ||     // sunflower
                data.getData() == 1 || // lilac
                data.getData() == 4 || // rose
                data.getData() == 5) { // peony
            // TODO
            // wait we can drop and drop 1 item
        }
    }
}
