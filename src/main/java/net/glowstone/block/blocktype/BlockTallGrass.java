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

public class BlockTallGrass extends BlockType implements IBlockGrowable {
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
        // TODO
        // have GlowKit implement DoublePlants MaterialData:
        // 0 = sunflower
        // 1 = lilac
        // 2 = grass (is 1 in GrassSpecies)
        // 3 = fern (is 2 in GrassSpecies)
        // 4 = rose
        // 5 = peony
        final MaterialData data = block.getState().getData();
        if (data instanceof LongGrass) {
            final LongGrass tallGrass = (LongGrass) data;
            final GrassSpecies species = tallGrass.getSpecies();
            if (species.equals(GrassSpecies.NORMAL) || species.equals(GrassSpecies.FERN_LIKE)) {
                block.setType(Material.DOUBLE_PLANT);
                block.setData((byte) (species.ordinal() + 1)); // FIXME
                block.getRelative(BlockFace.UP).setType(Material.DOUBLE_PLANT);
                block.getRelative(BlockFace.UP).setData((byte) 8); // FIXME
            }
        } else {
            warnMaterialData(LongGrass.class, data);
        }
     }
}
