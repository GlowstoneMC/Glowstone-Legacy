package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;

public class BlockDoublePlant extends BlockPlant {

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        if (super.canPlaceAt(block, against)
                && block.getRelative(BlockFace.UP).getType() == Material.AIR) {
            return true;
        }
        return false;
    }

    @Override
    public void afterPlace(GlowPlayer player, GlowBlock block, ItemStack holding) {
        final GlowBlockState headBlockState = block.getRelative(BlockFace.UP).getState();
        headBlockState.setType(Material.DOUBLE_PLANT);
        headBlockState.setRawData((byte) 8);
        headBlockState.update(true);
    }
}
