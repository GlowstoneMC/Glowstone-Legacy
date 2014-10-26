package net.glowstone.generator.objects.trees;

import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Material;
import org.bukkit.World;

public class TallRedwoodTree extends RedwoodTree {

    public TallRedwoodTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
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
        setHeight(random.nextInt(5) + 7);
        setLeavesHeight(height - random.nextInt(2) - 3);
        setMaxRadius(random.nextInt(height - leavesHeight + 1) + 1);
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
        int radius = 0;
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
            if (radius >= 1 && y == sourceY + leavesHeight + 1) {
                radius--;
            } else if (radius < maxRadius) {
                radius++;
            }
        }

        // generate the trunk
        for (int y = 0; y < height - 1; y++) {
            final Material type = delegate.getBlockState(world, sourceX, sourceY + y, sourceZ).getType();
            if (type == Material.AIR || type == Material.LEAVES) {
                delegate.setTypeAndRawData(world, sourceX, sourceY + y, sourceZ, Material.LOG, 1);
            }
        }

        // block below trunk is always dirt
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ, Material.DIRT, 0);

        return true;
    }
}
