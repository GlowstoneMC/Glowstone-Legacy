package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;

import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.LongGrass;
import org.bukkit.material.MaterialData;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class BlockTallGrass extends BlockPlant implements IBlockGrowable {
    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        if (random.nextFloat() < .125) {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(Material.SEEDS, 1)));
        }
        return Collections.unmodifiableList(Arrays.asList(new ItemStack[0]));
    }

    @Override
    public boolean canAbsorb(GlowBlock block, BlockFace face, ItemStack holding) {
        return true;
    }

    @Override
    public boolean canOverride(GlowBlock block, BlockFace face, ItemStack holding) {
        return true;
    }

    @Override
    public void fertilize(GlowBlock block) {
        final MaterialData data = block.getState().getData();
        if (data instanceof LongGrass) {
            final GrassSpecies species = ((LongGrass) data).getSpecies();
            if (species.equals(GrassSpecies.NORMAL) || species.equals(GrassSpecies.FERN_LIKE)) {
                block.setType(Material.DOUBLE_PLANT);
                block.setData((byte) (species.ordinal() + 1));
                block.getRelative(BlockFace.UP).setType(Material.DOUBLE_PLANT);
                block.getRelative(BlockFace.UP).setData((byte) 0x8);
            }
        } else {
            warnMaterialData(LongGrass.class, data);
        }
        // TODO
        // wait PR #27 (https://github.com/GlowstoneMC/Glowkit/pull/27) is
        // merged into Glowkit and uncomment below
        //final MaterialData data = block.getState().getData();
        //if (data instanceof LongGrass) {
        //    final GrassSpecies species = ((LongGrass) data).getSpecies();
        //    final DoublePlant plant = new DoublePlant();
        //    if (species.equals(GrassSpecies.NORMAL)) {
        //        plant.setSpecies(DoublePlantSpecies.DOUBLE_TALLGRASS);
        //    } else if (species.equals(GrassSpecies.FERN_LIKE)) {
        //        plant.setSpecies(DoublePlantSpecies.LARGE_FERN);
        //    } else {
        //        return;
        //    }
        //    block.setType(Material.DOUBLE_PLANT);
        //    final GlowBlockState blockState = block.getState();
        //    blockState.setData(plant);
        //    block.getRelative(BlockFace.UP).setType(Material.DOUBLE_PLANT);
        //    final GlowBlockState headBlockState = block.getRelative(BlockFace.UP).getState();
        //    final MaterialData headData = headBlockState.getData();
        //    ((DoublePlant) headData).setSpecies(DoublePlantSpecies.PLANT_APEX);
        //    headBlockState.setData(headData);
        //    blockState.update(true);
        //    headBlockState.update(true);
        //} else {
        //    warnMaterialData(LongGrass.class, data);
        //}
     }
}
