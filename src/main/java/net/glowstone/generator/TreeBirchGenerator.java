package net.glowstone.generator;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import net.glowstone.util.BlockStateDelegate;

public class TreeBirchGenerator extends TreeGenericGenerator {

    private final boolean tallBirch;

    public TreeBirchGenerator(BlockStateDelegate delegate) {
        this(false, delegate);
    }

    public TreeBirchGenerator(boolean tallBirch, BlockStateDelegate delegate) {
        super(delegate);
        this.tallBirch = tallBirch;
    }

    public boolean generate(Random random, Location loc) {

        int height = random.nextInt(3) + 5;
        if (tallBirch) {
            height += random.nextInt(7);
        }
        final int sourceX = loc.getBlockX();
        final int sourceY = loc.getBlockY();
        final int sourceZ = loc.getBlockZ();

        // check height range
        if (sourceY < 1 || sourceY + height + 1 > 255) {
            return false;
        }

        final World world = loc.getWorld();

        // check below block
        Block b = world.getBlockAt(sourceX, sourceY - 1, sourceZ);
        if ((!b.getType().equals(Material.GRASS)
                && !b.getType().equals(Material.DIRT)
                && !b.getType().equals(Material.SOIL))
                || sourceY >= 256 - height - 1) {
            return false;
        }

        int x, y, z, radius;
        // check around if there's enough space
        for (y = sourceY; y <= sourceY + 1 + height; y++) {
            // Space requirement
            radius = 1; // default radius if above first block
            if (y == sourceY) {
                radius = 0; // radius at source block y is 0 (only trunk)
            } else if (y >= sourceY + 1 + height - 2) {
                radius = 2; // max radius starting at leaves bottom
            }
            // check for block collision on horizontal slices
            for (x = sourceX - radius; x <= sourceX + radius; x++) {
                for (z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if (y >= 0 && y < 256) {
                        b = world.getBlockAt(x, y, z);
                        // we can overlap some blocks around
                        if (!isOverridableBlock(b)) {
                            return false;
                        }
                    } else { // height out of range
                        return false;
                    }
                }
            }
        }

        // generate the leaves
        int nX, nY, nZ;
        for (y = sourceY + height - 3; y <= sourceY + height; y++) {
            nY = y - (sourceY + height);
            radius = 1 - nY / 2;
            for (x = sourceX - radius; x <= sourceX + radius; x++) {
                nX = x - sourceX;
                for (z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    nZ = z - sourceZ;
                    if (Math.abs(nX) != radius || Math.abs(nZ) != radius
                            || (random.nextBoolean() && nY != 0)) {
                        final Material material = world.getBlockAt(x, y, z).getType();
                        if (material.equals(Material.AIR) || material.equals(Material.LEAVES)) {
                            delegate.setTypeAndRawData(world, x, y, z, Material.LEAVES, 2);
                        }
                    }
                }
            }
        }

        // generate the trunk
        for (y = 0; y < height; y++) {
            final Material material = world.getBlockAt(sourceX, sourceY + y, sourceZ).getType();
            if (material.equals(Material.AIR) || material.equals(Material.LEAVES)) {
                delegate.setTypeAndRawData(world, sourceX, sourceY + y, sourceZ, Material.LOG, 2);
            }
        }

        // block below trunk is always dirt
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ, Material.DIRT, 0);

        return true;
    }
}
