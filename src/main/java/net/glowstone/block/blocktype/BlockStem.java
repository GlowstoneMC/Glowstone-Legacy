package net.glowstone.block.blocktype;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import net.glowstone.block.GlowBlock;

public class BlockStem extends BlockPlant {
    private Material fruitType;
    private Material seedsType;

    public BlockStem(Material plantType) {
        if (plantType == Material.MELON_STEM) {
            fruitType = Material.MELON_BLOCK;
            seedsType = Material.MELON_SEEDS;
        } else if (plantType == Material.PUMPKIN_STEM) {
            fruitType = Material.PUMPKIN;
            seedsType = Material.PUMPKIN_SEEDS;
        } else throw new IllegalArgumentException("Invalid plant material " + plantType.name() + " for BlockStem");
    }

    @Override
    public boolean canPlaceAt(GlowBlock block, BlockFace against) {
        if (block.getRelative(BlockFace.DOWN).getType() == Material.SOIL) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<ItemStack> getDrops(GlowBlock block) {
        if (block.getState().getRawData() >= CropState.RIPE.ordinal()) {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack(seedsType, random.nextInt(4))));
        } else {
            return Collections.unmodifiableList(Arrays.asList(new ItemStack[0]));
        }
    }
}
