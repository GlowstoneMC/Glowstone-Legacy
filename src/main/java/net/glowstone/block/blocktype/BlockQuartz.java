package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.MaterialMatcher;
import net.glowstone.inventory.ToolType;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockQuartz extends BlockNeedsTool {

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        if (holding.getDurability() > 1) {
            switch (face) {
                case NORTH:
                case SOUTH:
                    state.setRawData((byte) 4);
                    break;
                case WEST:
                case EAST:
                    state.setRawData((byte) 3);
                    break;
                case UP:
                case DOWN:
                    state.setRawData((byte) 2);
                    break;
            }
        }
    }

    @Override
    protected MaterialMatcher getNeededMiningTool(GlowBlock block) {
        return ToolType.PICKAXE;
    }
}
