package net.glowstone.generator.decorators;

import java.util.Random;

import net.glowstone.generator.objects.DesertWell;

import org.bukkit.Chunk;
import org.bukkit.World;

public class DesertWellDecorator extends BlockDecorator {

    @Override
    public void decorate(World world, Random random, Chunk source) {
        if (random.nextInt(1000) == 0) {
            int x = (source.getX() << 4) + random.nextInt(16);
            int z = (source.getZ() << 4) + random.nextInt(16);
            int y = world.getHighestBlockYAt(x, z);
            new DesertWell().generate(world, x, y, z);
        }
    }
}
