package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

import net.glowstone.block.GlowBlock;

public class BlockMushroom extends BlockType {

    private final Material mushroomType;

    public BlockMushroom(Material mushroomType) {
        this.mushroomType = mushroomType;
    }

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        final GlowBlock belowBlock = block.getRelative(BlockFace.DOWN);
        final Material type = belowBlock.getType();
        if (type == Material.GRASS ||
                (type == Material.DIRT && belowBlock.getData() != 2)) {
            if (block.getLightLevel() < 13) { // checking light level for dirt, coarse dirt and grass
                return true;
            }
        } else if (type == Material.MYCEL ||
                (type == Material.DIRT && belowBlock.getData() == 2)) {
            // not checking light level if mycel or podzol
            return true;
        }
        return false;
    }
}
