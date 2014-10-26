package net.glowstone.generator.objects.trees;

import java.util.Random;

import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Material;
import org.bukkit.World;

public class DarkOakTree extends AcaciaTree {

    public DarkOakTree(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
        int height = random.nextInt(2) + random.nextInt(3) + 6;
        setHeight(height);
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

        int n = random.nextInt(4);
        int twistHeight = height - random.nextInt(4);
        int twistCount = random.nextInt(3);
        int centerX = sourceX, centerZ = sourceZ;
        int trunkTopY = 0;

        // generates the trunk
        for (int y = 0; y < height; y++) {

            // trunk twists
            if (twistCount > 0 && y >= twistHeight) {
                centerX += getDirectionalModX(n);
                centerZ += getDirectionalModZ(n);
                twistCount--;
            }

            final Material material = delegate.getBlockState(world, centerX, sourceY + y, centerZ).getType();
            if (material == Material.AIR || material == Material.LEAVES) {
                trunkTopY = sourceY + y;
                // SELF, SOUTH, EAST, SOUTH EAST
                delegate.setTypeAndRawData(world, centerX, sourceY + y, centerZ, Material.LOG_2, 1);
                delegate.setTypeAndRawData(world, centerX, sourceY + y, centerZ + 1, Material.LOG_2, 1);
                delegate.setTypeAndRawData(world, centerX + 1, sourceY + y, centerZ, Material.LOG_2, 1);
                delegate.setTypeAndRawData(world, centerX + 1, sourceY + y, centerZ + 1, Material.LOG_2, 1);
            }
        }

        // generates leaves
        for (int x = -2; x <= 0; x++) {
            for (int z = -2; z <= 0; z++) {
                if ((x != -1 || z != -2) && (x > -2 || z > -1)) {
                    setLeaveAt(world, centerX + x, trunkTopY + 1, centerZ + z);
                    setLeaveAt(world, 1 + centerX - x, trunkTopY + 1, centerZ + z);
                    setLeaveAt(world, centerX + x, trunkTopY + 1, 1 + centerZ - z);
                    setLeaveAt(world, 1 + centerX - x, trunkTopY + 1, 1 + centerZ - z);
                }
                setLeaveAt(world, centerX + x, trunkTopY - 1, centerZ + z);
                setLeaveAt(world, 1 + centerX - x, trunkTopY - 1, centerZ + z);
                setLeaveAt(world, centerX + x, trunkTopY - 1, 1 + centerZ - z);
                setLeaveAt(world, 1 + centerX - x, trunkTopY - 1, 1 + centerZ - z);                
            }
        }

        // finish leaves below the canopy
        for (int x = -3; x <= 4; x++) {
            for (int z = -3; z <= 4; z++) {
                if (Math.abs(x) < 3 || Math.abs(z) < 3) {
                    setLeaveAt(world, centerX + x, trunkTopY, centerZ + z);
                }
            }
        }

        // generates some trunk excrescences
        for (int x = -1; x <= 2; x++) {
            for (int z = -1; z <= 2; z++) {
                if ((x == -1 || z == -1 || x == 2 || z == 2) && random.nextInt(3) == 0) {
                    for (int y = 0; y < random.nextInt(3) + 2; y++) {
                        final Material material = delegate.getBlockState(world, sourceX + x, trunkTopY - y - 1, sourceZ + z).getType();
                        if (material == Material.AIR || material == Material.LEAVES) {
                            delegate.setTypeAndRawData(world, sourceX + x, trunkTopY - y - 1, sourceZ + z, Material.LOG_2, 1);
                        }
                    }

                    // leaves below the canopy
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            setLeaveAt(world, centerX + x + i, trunkTopY, centerZ + z + j);
                        }
                    }
                    for (int i = -2; i <= 2; i++) {
                        for (int j = -2; j <= 2; j++) {
                            if ((Math.abs(i) < 2) || (Math.abs(j) < 2)) {
                                setLeaveAt(world, centerX + x + i, trunkTopY - 1, centerZ + z + j);
                            }
                        }
                    }
                }
            }
        }

        // 50% chance to have a 4 leaves cap on the center of the canopy
        if (random.nextInt(2) == 0) {
            setLeaveAt(world, centerX, trunkTopY + 2, centerZ);
            setLeaveAt(world, centerX + 1, trunkTopY + 2, centerZ);
            setLeaveAt(world, centerX + 1, trunkTopY + 2, centerZ + 1);
            setLeaveAt(world, centerX, trunkTopY + 2, centerZ + 1);
        }

        // block below trunk is always dirt (SELF, SOUTH, EAST, SOUTH EAST)
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ, Material.DIRT, 0);
        delegate.setTypeAndRawData(world, sourceX, sourceY - 1, sourceZ + 1, Material.DIRT, 0);
        delegate.setTypeAndRawData(world, sourceX + 1, sourceY - 1, sourceZ, Material.DIRT, 0);
        delegate.setTypeAndRawData(world, sourceX + 1, sourceY - 1, sourceZ + 1, Material.DIRT, 0);

        return true;
    }

    private void setLeaveAt(World world, int x, int y, int z) {
        if (delegate.getBlockState(world, x, y, z).getType() == Material.AIR) {
            delegate.setTypeAndRawData(world, x, y, z, Material.LEAVES_2, 1);
        }
    }
}
