package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class BlockSnow extends BlockType {

    @Override
    public void placeBlock(GlowPlayer player, GlowBlockState state, BlockFace face, ItemStack holding, Vector clickedLoc) {

        /* We get the block state and data value of the block under the target. */
        GlowBlockState snowstate = state.getBlock().getRelative(BlockFace.DOWN).getState();
        byte data = snowstate.getRawData();

        if ((snowstate.getType() == Material.SNOW) && (data < 7)) {

            /* If the block we got is snow and it does not have more than 7 layers, increment the block's data value. */
            data++;

            snowstate.setRawData(data);
            snowstate.update();
        } else if (snowstate.getType() != Material.SNOW) {

            /* Else if the block is not snow, place the block on top of the snow. */
            super.placeBlock(player, state, face, holding, clickedLoc);
        }
    }
}
