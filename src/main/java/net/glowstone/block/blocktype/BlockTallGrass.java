package net.glowstone.block.blocktype;

import net.glowstone.block.GlowBlock;
import net.glowstone.block.GlowBlockState;

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
    public boolean isFertilizable(GlowBlock block) {
        final MaterialData data = block.getState().getData();
        if (data instanceof LongGrass) {
            if (((LongGrass) data).getSpecies() != GrassSpecies.DEAD) {
                return true;
            }
        } else {
            warnMaterialData(LongGrass.class, data);
        }
        return false;
    }

    @Override
    public boolean canGrowWithChance(GlowBlock block) {
        return true;
    }

    @Override
    public void grow(GlowBlock block) {
        final MaterialData data = block.getState().getData();
        if (data instanceof LongGrass) {
            final GrassSpecies species = ((LongGrass) data).getSpecies();
            if (species.equals(GrassSpecies.NORMAL) || species.equals(GrassSpecies.FERN_LIKE)) {
                final GlowBlockState blockState = block.getState();
                final GlowBlockState headBlockState = block.getRelative(BlockFace.UP).getState();
                blockState.setType(Material.DOUBLE_PLANT);
                blockState.setRawData((byte) (species.ordinal() + 1));
                headBlockState.setType(Material.DOUBLE_PLANT);
                headBlockState.setRawData((byte) 8);
                // TODO
                // call onBlockGrow from EventFactory
                blockState.update(true);
                headBlockState.update(true);
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
        //    final GlowBlockState blockState = block.getState();
        //    final GlowBlockState headBlockState = block.getRelative(BlockFace.UP).getState();
        //    blockState.setType(Material.DOUBLE_PLANT);
        //    blockState.setData(plant);
        //    headBlockState.setType(Material.DOUBLE_PLANT);
        //    headBlockState.setData(new DoublePlant(DoublePlantSpecies.PLANT_APEX));
        //    // TODO
        //    // call onBlockGrow from EventFactory
        //    blockState.update(true);
        //    headBlockState.update(true);
        //} else {
        //    warnMaterialData(LongGrass.class, data);
        //}
     }
}
