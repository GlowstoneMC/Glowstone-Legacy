package net.glowstone.block.blocktype;

import java.util.Random;

import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.material.LongGrass;

import net.glowstone.GlowWorld;
import net.glowstone.block.GlowBlock;

public class BlockGrass extends BlockDirectDrops implements IBlockGrowable {

    public BlockGrass(Material dropType) {
        super(dropType);
    }

    // TODO
    // maybe use GlowWorld random instance instead
    private final Random random = new Random();

    @Override
    public void fertilize(GlowBlock block) {
        final GlowWorld world = block.getWorld();

        int i = 0;
        do {
            int x = block.getX();
            int y = block.getY() + 1;
            int z = block.getZ();
            int j = 0;

            while (true) {
                // if there's available space
                if (world.getBlockAt(x, y, z).getType().equals(Material.AIR)) {
                    if (random.nextInt(8) == 0 && world.getHighestBlockYAt(x, z) == y) {
                        // sometimes grow random flower if highest block
                        // TODO
                        // choose random flowers and consider the biome
                        if (random.nextInt(2) == 0) {
                            world.getBlockAt(x, y, z).setType(Material.RED_ROSE);
                        } else {
                            world.getBlockAt(x, y, z).setType(Material.YELLOW_FLOWER);
                        }
                    } else if (world.getHighestBlockYAt(x, z) == y) {
                        // grow tall grass if highest block
                        world.getBlockAt(x, y, z).setType(Material.LONG_GRASS);
                        world.getBlockAt(x, y, z).setData(
                                new LongGrass(GrassSpecies.NORMAL).getData());
                    }
                } else if (j < i / 16) { // look around for grass block
                    x += random.nextInt(3) - 1;
                    y += (random.nextInt(3) - 1) * random.nextInt(3) / 2;
                    z += random.nextInt(3) - 1;
                    if (world.getBlockAt(x, y - 1, z).getType().equals(Material.GRASS)) {
                        j++;
                        continue;
                    }
                }
                i++;
                break;
            }
        } while (i < 128);
    }
}
