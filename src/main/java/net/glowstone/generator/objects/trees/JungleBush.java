package net.glowstone.generator.objects.trees;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import net.glowstone.util.BlockStateDelegate;

public class JungleBush extends GenericTree {

    public JungleBush(Random random, BlockStateDelegate delegate) {
        super(random, delegate);
        setTypes(3, 0);
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
    public boolean generate(World world, int sourceX, int sourceY, int sourceZ) {

        while ((world.getBlockAt(sourceX, sourceY, sourceZ).getType() == Material.AIR ||
                world.getBlockAt(sourceX, sourceY, sourceZ).getType() == Material.LEAVES) &&
                sourceY > 0) {
            sourceY--;
        }

        // check below block
        if (!canPlaceOn(world, sourceX, sourceY, sourceZ)) {
            return false;
        }

        // generates the trunk
        delegate.setTypeAndRawData(world, sourceX, sourceY + 1, sourceZ, Material.LOG, logType);

        // generates the leaves
        for (int y = sourceY + 1; y <= sourceY + 3; y++) {
            int radius = 3 - (y - sourceY);

            for (int x = sourceX - radius; x <= sourceX + radius; x++) {
                for (int z = sourceZ - radius; z <= sourceZ + radius; z++) {
                    if ((Math.abs(x - sourceX) != radius || Math.abs(z - sourceZ) != radius || random.nextBoolean()) &&
                            !delegate.getBlockState(world, x, y, z).getType().isSolid()) {
                        delegate.setTypeAndRawData(world, x, y, z, Material.LEAVES, leavesType);
                    }
                }
            }
        }

        return true;
    }
}
