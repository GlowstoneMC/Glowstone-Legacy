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
        GlowBlockState snowstate = state.getBlock().getRelative(BlockFace.DOWN).getState();
        byte data = snowstate.getRawData();
        if (snowstate.getType() == Material.SNOW && data < 7) {
            data++;
            snowstate.setRawData(data);
            snowstate.update();
        } else if (snowstate.getType() != Material.SNOW) {
            super.placeBlock(player, state, face, holding, clickedLoc);
        }
    }
}
