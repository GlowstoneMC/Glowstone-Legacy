package net.glowstone.block.blocktype;

import net.glowstone.GlowChunk;
import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.block.entity.TEFurnace;
import net.glowstone.block.entity.TileEntity;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.MaterialMatcher;
import net.glowstone.inventory.ToolType;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Furnace;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class BlockFurnace extends BlockContainer {

    @Override
    public TileEntity createTileEntity(GlowChunk chunk, int cx, int cy, int cz) {
        return new TEFurnace(chunk.getBlock(cx, cy, cz));
    }

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);
        final MaterialData data = state.getData();
        if (data instanceof Furnace) {
            ((Furnace) data).setFacingDirection(getOppositeBlockFace(player.getLocation(), false));
            state.setData(data);
        } else {
            warnMaterialData(Furnace.class, data);
        }
    }

    @Override
    protected MaterialMatcher getNeededMiningTool(GlowBlock block) {
        return ToolType.PICKAXE;
    }
}
