package net.glowstone.generator.objects.trees;

import net.glowstone.util.BlockStateDelegate;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class GenericTree {

    protected final Random random;
    protected int height;
    protected int logType;
    protected int leavesType;
    protected final BlockStateDelegate delegate;
    protected Collection<Material> overridables;

    public GenericTree(Random random, BlockStateDelegate delegate) {
        this.random = random;
        this.delegate = delegate;
        setOverridables(
                Material.AIR,
                Material.LEAVES,
                Material.GRASS,
                Material.DIRT,
                Material.LOG,
                Material.LOG_2,
                Material.SAPLING,
                Material.VINE
        );
        setHeight(random.nextInt(3) + 4);
        setTypes(0, 0);
    }

    protected final void setOverridables(Material... overridables) {
        this.overridables = Arrays.asList(overridables);
    }

    protected final void setHeight(int height) {
        this.height = height;
    }

    protected final void setTypes(int logType, int leavesType) {
        this.logType = logType;
        this.leavesType = leavesType;
    }

    public boolean canHeightFitAt(int sourceY) {
        return sourceY >= 1 && sourceY + height + 1 <= 255;
    }

    public boolean canPlaceOn(World world, int x, int y, int z) {
        final BlockState state = delegate.getBlockState(world, x, y, z);
        return state.getType() == Material.GRASS ||
                state.getType() == Material.DIRT ||
                state.getType() == Material.SOIL;
    }

    public boolean canPlaceAt(World world, int sourceX, int sourceY, int sourceZ) {
        for (int y = sourceY; y <= sourceY + 1 + height; y++) {
            // Space requirement
            int radius = 1; // default radius if above first block
            if (y == sourceY) {
                radius = 0; // radius at source block y is 0 (only trunk)
            } else if (y >= sourceY + 1 + height - 2) {
                radius = 2; // max radius starting at leaves bottom
            }
            // check for block collision on horizontal slices
            for (int x = sourceX - radius; x <= sourceX + radius; x++) {
                for (int z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if (y >= 0 && y < 256) {
                        // we can overlap some blocks around
                        final Material type = delegate.getBlockState(world, x, y, z).getType();
                        if (!overridables.contains(type)) {
                            return false;
                        }
                    } else { // height out of range
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean generate(World world, int sourceX, int sourceY, int sourceZ) {
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
            int radius = 1 - n / 2;
            for (int x = sourceX - radius; x <= sourceX + radius; x++) {
                for (int z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if (Math.abs(x - sourceX) != radius || Math.abs(z - sourceZ) != radius
                            || (random.nextBoolean() && n != 0)) {
                        final Material material = delegate.getBlockState(world, x, y, z).getType();
                        if (material == Material.AIR || material == Material.LEAVES) {
                            delegate.setTypeAndRawData(world, x, y, z, Material.LEAVES, leavesType);
                        }
                    }
                }
            }
        }

        // generate the trunk
        for (int y = 0; y < height; y++) {
            final Material material = delegate.getBlockState(world, sourceX, sourceY + y, sourceZ).getType();
            if (material == Material.AIR || material == Material.LEAVES) {
                delegate.setTypeAndRawData(world, sourceX, sourceY + y, sourceZ, Material.LOG, logType);
            }
        }

        // block below trunk is always dirt
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ, Material.DIRT, 0);

        return true;
    }
}
