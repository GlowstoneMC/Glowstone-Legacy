package net.glowstone.generator.objects.trees;

import net.glowstone.util.BlockStateDelegate;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Random;

public class RedwoodTree extends GenericTree {

    protected int maxRadius;
    protected int leavesHeight;

    public RedwoodTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
        setOverridables(
                Material.AIR,
                Material.LEAVES
        );
        setHeight(random.nextInt(4) + 6);
        setLeavesHeight(random.nextInt(2) + 1);
        setMaxRadius(random.nextInt(2) + 2);
    }

    protected final void setMaxRadius(int maxRadius) {
        this.maxRadius = maxRadius;
    }

    protected final void setLeavesHeight(int leavesHeight) {
        this.leavesHeight = leavesHeight;
    }

    @Override
    public boolean canPlaceAt(World world, int sourceX, int sourceY, int sourceZ) {
        for (int y = sourceY; y <= sourceY + 1 + height; y++) {
            // Space requirement
            int radius; // default radius if above first block
            if (y - sourceY < leavesHeight) {
                radius = 0; // radius is 0 for trunk below leaves
            } else {
                radius = maxRadius;
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

    @Override
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
        int radius = random.nextInt(2);
        int peakRadius = 1;
        int minRadius = 0;
        for (int y = sourceY + height; y >= sourceY + leavesHeight; y--) {
            // leaves are built from top to bottom
            for (int x = sourceX - radius; x <= sourceX + radius; x++) {
                for (int z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if ((Math.abs(x - sourceX) != radius || Math.abs(z - sourceZ) != radius || radius <= 0) &&
                            delegate.getBlockState(world, x, y, z).getType() == Material.AIR) {
                        delegate.setTypeAndRawData(world, x, y, z, Material.LEAVES, 1);
                    }
                }
            }
            if (radius >= peakRadius) {
                radius = minRadius;
                minRadius = 1; // after the peak radius is reached once, the min radius increases
                peakRadius++;  // the peak radius increases each time it's reached
                if (peakRadius > maxRadius) {
                    peakRadius = maxRadius;
                }
            } else {
                radius++;
            }
        }

        // generate the trunk
        for (int y = 0; y < height - random.nextInt(3); y++) {
            final Material type = delegate.getBlockState(world, sourceX, sourceY + y, sourceZ).getType();
            if (overridables.contains(type)) {
                delegate.setTypeAndRawData(world, sourceX, sourceY + y, sourceZ, Material.LOG, 1);
            }
        }

        // block below trunk is always dirt
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ, Material.DIRT, 0);

        return true;
    }
}
