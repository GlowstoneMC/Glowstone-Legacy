package net.glowstone.generator.trees;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import net.glowstone.util.BlockStateDelegate;

public class BirchTree extends GenericTree {

    public BirchTree(Random random, BlockStateDelegate delegate) {
        this(random, false, delegate);
    }

    public BirchTree(Random random, boolean tallBirch, BlockStateDelegate delegate) {
        super(random, delegate);
        int height = random.nextInt(3) + 5;
        if (tallBirch) {
            height += random.nextInt(7);
        }
        setHeight(height);
    }

    @Override
    public boolean generate(Location loc) {

        final int sourceX = loc.getBlockX();
        final int sourceY = loc.getBlockY();
        final int sourceZ = loc.getBlockZ();

        // check height range
        if (!canHeightFitAt(sourceY)) {
            return false;
        }

        final World world = loc.getWorld();

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
                        if (material.equals(Material.AIR) || material.equals(Material.LEAVES)) {
                            delegate.setTypeAndRawData(world, x, y, z, Material.LEAVES, 2);
                        }
                    }
                }
            }
        }

        // generate the trunk
        for (int y = 0; y < height; y++) {
            final Material material = delegate.getBlockState(world, sourceX, sourceY + y, sourceZ).getType();
            if (material.equals(Material.AIR) || material.equals(Material.LEAVES)) {
                delegate.setTypeAndRawData(world, sourceX, sourceY + y, sourceZ, Material.LOG, 2);
            }
        }

        // block below trunk is always dirt
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ, Material.DIRT, 0);

        return true;
    }
}
