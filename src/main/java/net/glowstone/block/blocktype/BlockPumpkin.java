package net.glowstone.block.blocktype;

import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Pumpkin;
import org.bukkit.util.Vector;

import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;

public class BlockPumpkin extends BlockType {

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {
        super.placeBlock(player, state, face, holding, clickedLoc);

        final MaterialData data = state.getData();
        if (data instanceof Pumpkin) {
            ((Pumpkin) data).setFacingDirection(getOppositeBlockFace(player.getLocation(), false).getOppositeFace());
            state.setData(data);
        } else {
            warnMaterialData(Pumpkin.class, data);
        }
    }
}
