package net.glowstone.generator.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class SugarCanePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk source) {

        for (int i = 0; i < 10; i++) {
            int sourceX = (source.getX() << 4) + random.nextInt(16);
            int sourceZ = (source.getZ() << 4) + random.nextInt(16);
            int sourceY = random.nextInt(world.getHighestBlockYAt(sourceX, sourceZ) << 1);

            for (int j = 0; j < 20; j++) {
                int x = sourceX + random.nextInt(4) - random.nextInt(4);
                int z = sourceZ + random.nextInt(4) - random.nextInt(4);
                if (world.getBlockAt(x, sourceY, z).getType().equals(Material.AIR)
                        // needs a directly adjacent stationary water block
                        && (world.getBlockAt(x - 1, sourceY - 1, z).getType().equals(Material.STATIONARY_WATER)
                        || world.getBlockAt(x + 1, sourceY - 1, z).getType().equals(Material.STATIONARY_WATER)
                        || world.getBlockAt(x, sourceY - 1, z - 1).getType().equals(Material.STATIONARY_WATER)
                        || world.getBlockAt(x, sourceY - 1, z + 1).getType().equals(Material.STATIONARY_WATER)
                        // or a directly adjacent flowing water block
                        || world.getBlockAt(x - 1, sourceY - 1, z).getType().equals(Material.WATER)
                        || world.getBlockAt(x + 1, sourceY - 1, z).getType().equals(Material.WATER)
                        || world.getBlockAt(x, sourceY - 1, z - 1).getType().equals(Material.WATER)
                        || world.getBlockAt(x, sourceY - 1, z + 1).getType().equals(Material.WATER))) {

                    for (int n = 0; n <= random.nextInt(random.nextInt(3) + 1) + 1; n++) {
                        final Material type = world.getBlockAt(x, sourceY - 1, z).getType();
                        if (type.equals(Material.SUGAR_CANE_BLOCK) || type.equals(Material.GRASS)
                                || (type.equals(Material.DIRT) && world.getBlockAt(x, sourceY - 1, z).getData() == 0)
                                || type.equals(Material.SAND)) {
                            final Block block = world.getBlockAt(x, sourceY + n, z);
                            block.setType(Material.SUGAR_CANE_BLOCK);
                            block.setData((byte) 0);
                        }
                    }
                }
            }
        }
    }
}
