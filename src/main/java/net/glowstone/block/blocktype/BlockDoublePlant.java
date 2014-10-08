package net.glowstone.block.blocktype;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import net.glowstone.block.GlowBlock;
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
        block.getRelative(BlockFace.UP).setType(Material.DOUBLE_PLANT);
        block.getRelative(BlockFace.UP).setData((byte) 0x8);
        // TODO
        // wait PR #27 (https://github.com/GlowstoneMC/Glowkit/pull/27) is
        // merged into Glowkit and uncomment below
        //final GlowBlock headBlock = block.getRelative(BlockFace.UP);
        //final MaterialData data = block.getState().getData();
        //if (data instanceof DoublePlant) {
        //    headBlock.setType(Material.DOUBLE_PLANT);
        //    GlowBlockState headBlockState = headBlock.getState();
        //    MaterialData headData = headBlockState.getData();
        //    ((DoublePlant) headData).setSpecies(DoublePlantSpecies.PLANT_APEX);
        //    headBlockState.setData(headData);
        //    headBlockState.update(true);
        //} else {
        //    warnMaterialData(DoublePlant.class, data);
        //}
    }

    @Override
    public void fertilize(GlowBlock block) {
        MaterialData data = block.getState().getData();
        if (data.getData() == 8) { // above block
            data = block.getRelative(BlockFace.DOWN).getState().getData();
        }
        if (data.getData() == 0 ||     // sunflower
                data.getData() == 1 || // lilac
                data.getData() == 4 || // rose
                data.getData() == 5) { // peony
            block.getWorld().dropItemNaturally(block.getLocation(),
                    new ItemStack(Material.DOUBLE_PLANT, 1, (short) data.getData()));
        }
        // TODO
        // wait PR #27 (https://github.com/GlowstoneMC/Glowkit/pull/27) is
        // merged into Glowkit and uncomment below
        //MaterialData data = block.getState().getData();
        //if (data instanceof DoublePlant) {
        //    DoublePlantSpecies species = ((DoublePlant) data).getSpecies();
        //    if (species.equals(DoublePlantSpecies.PLANT_APEX)) {
        //        data = block.getRelative(BlockFace.DOWN).getState().getData();
        //        species = ((DoublePlant) data).getSpecies();
        //    }
        //    if (species.equals(DoublePlantSpecies.SUNFLOWER)
        //            || species.equals(DoublePlantSpecies.LILAC)
        //            || species.equals(DoublePlantSpecies.ROSE_BUSH)
        //            || species.equals(DoublePlantSpecies.PEONY)) {
        //        block.getWorld().dropItemNaturally(block.getLocation(),
        //                new ItemStack(Material.DOUBLE_PLANT, 1, (short) species.ordinal()));
        //    }
        //} else {
        //    warnMaterialData(DoublePlant.class, data);
        //}
    }
}
