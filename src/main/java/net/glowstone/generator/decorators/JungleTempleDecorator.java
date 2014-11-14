package net.glowstone.generator.decorators;

import java.util.Random;

import net.glowstone.constants.GlowStructure;
import net.glowstone.generator.StructureGenerator;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class JungleTempleDecorator extends BlockDecorator {

    @Override
    public void decorate(World world, Random random, Chunk source) {
        if (random.nextInt(2000) == 0) {
            int x = (source.getX() << 4) + random.nextInt(16);
            int z = (source.getZ() << 4) + random.nextInt(16);
            int y = world.getHighestBlockYAt(x, z);
            new StructureGenerator().generate(random, new Location(world, x, y, z), GlowStructure.JUNGLE_TEMPLE);
        }
    }
}
