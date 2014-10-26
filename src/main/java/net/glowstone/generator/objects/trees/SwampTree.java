package net.glowstone.generator.objects.trees;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import net.glowstone.util.BlockStateDelegate;

public class SwampTree extends GenericTree {

    public SwampTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
        setOverridables(
                Material.AIR,
                Material.LEAVES
        );
        int height = random.nextInt(4) + 5;
        setHeight(height);
    }

    @Override
    public boolean canPlaceOn(World world, int x, int y, int z) {
        final BlockState state = delegate.getBlockState(world, x, y, z);
        if (state.getType() != Material.GRASS
                && state.getType() != Material.DIRT) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canPlaceAt(World world, int sourceX, int sourceY, int sourceZ) {
        for (int y = sourceY; y <= sourceY + 1 + height; y++) {
            // Space requirement
            int radius = 1; // default radius if above first block
            if (y == sourceY) {
                radius = 0; // radius at source block y is 0 (only trunk)
            } else if (y >= sourceY + 1 + height - 2) {
                radius = 3; // max radius starting at leaves bottom
            }
            // check for block collision on horizontal slices
            for (int x = sourceX - radius; x <= sourceX + radius; x++) {
                for (int z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if (y >= 0 && y < 256) {
                        // we can overlap some blocks around
                        final Material type = delegate.getBlockState(world, x, y, z).getType();
                        if (!overridables.contains(type)) {
                            // the trunk can be immersed by 1 block of water
                            if (type == Material.WATER || type == Material.STATIONARY_WATER) {
                                if (y > sourceY) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                    } else { // height out of range
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean generate(World world, int sourceX, int sourceY, int sourceZ) {

        while ((world.getBlockAt(sourceX, sourceY - 1, sourceZ).getType() == Material.WATER ||
                world.getBlockAt(sourceX, sourceY - 1, sourceZ).getType() == Material.STATIONARY_WATER)) {
            sourceY--;
        }

        // check height range
        if (!canHeightFitAt(sourceY)) {
            return false;
        }

        // check below block
        if (!canPlaceOn(world, sourceX, sourceY - 1, sourceZ)) {
            return false;
        }

        // check for sufficient space around
        if (!canPlaceAt(world, sourceX, sourceY, sourceZ)) {
            return false;
        }

        // generate the leaves
        for (int y = sourceY + height - 3; y <= sourceY + height; y++) {
            int n = y - (sourceY + height);
            int radius = 2 - n / 2;
            for (int x = sourceX - radius; x <= sourceX + radius; x++) {
                for (int z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if (Math.abs(x - sourceX) != radius || Math.abs(z - sourceZ) != radius
                            || (random.nextBoolean() && n != 0)) {
                        final Material material = delegate.getBlockState(world, x, y, z).getType();
                        if (material == Material.AIR || material == Material.LEAVES) {
                            delegate.setTypeAndRawData(world, x, y, z, Material.LEAVES, 0);
                        }
                    }
                }
            }
        }

        // generate the trunk
        for (int y = 0; y < height; y++) {
            final Material material = delegate.getBlockState(world, sourceX, sourceY + y, sourceZ).getType();
            if (material == Material.AIR || material == Material.LEAVES ||
                    material == Material.WATER || material == Material.STATIONARY_WATER) {
                delegate.setTypeAndRawData(world, sourceX, sourceY + y, sourceZ, Material.LOG, 0);
            }
        }

        // add some vines on the leaves
        addVinesOnLeaves(world, sourceX, sourceY, sourceZ);

        // block below trunk is always dirt
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ, Material.DIRT, 0);

        return true;
    }
}
