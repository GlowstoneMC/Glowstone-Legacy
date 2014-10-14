package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;
import net.glowstone.entity.GlowPlayer;

public class BlockDoublePlant extends BlockPlant implements IBlockGrowable {

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        if (super.canPlaceAt(block, against)
                && block.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
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

    @Override
    public boolean isFertilizable(GlowBlock block) {
        int data = block.getData();
        if (data == 8) { // above part
            data = block.getRelative(BlockFace.DOWN).getState().getRawData();
        }
        if (data != 2 && // double tall grass
            data != 3) { // large fern
            return true;
        }
        return false;
    }

    @Override
    public boolean canGrowWithChance(GlowBlock block) {
        return true;
    }

    @Override
    public void grow(GlowPlayer player, GlowBlock block) {
        int data = block.getData();
        if (data == 8) { // above part
            data = block.getRelative(BlockFace.DOWN).getState().getRawData();
        }
        if (data == 0 ||     // sunflower
                data == 1 || // lilac
                data == 4 || // rose
                data == 5) { // peony
            block.getWorld().dropItemNaturally(block.getLocation(),
                    new ItemStack(Material.DOUBLE_PLANT, 1, (short) data));
        }
    }
}
