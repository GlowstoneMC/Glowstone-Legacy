package net.glowstone.generator.populators;

import net.glowstone.generator.structures.Dungeon;
import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import java.util.Random;

/**
 * A BlockPopulator that places dungeons around the map.
 */
public class DungeonPopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk source) {
        SimplexNoiseGenerator noise = new SimplexNoiseGenerator(world);
        double density = noise.noise(source.getX(), source.getZ());
        if (density > 0.8) {
        // TODO
        // later switch to this loop to have 8 attempts of dungeon
        // placement per chunk
        //for (int i = 0; i < 8; i++) {
            int x = (source.getX() << 4) + random.nextInt(16);
            int z = (source.getZ() << 4) + random.nextInt(16);
            int y = random.nextInt(256);
            BlockStateDelegate delegate = new BlockStateDelegate();
            new Dungeon(random, new Location(world, x, y, z), delegate).generate();
            delegate.updateBlockStates();
        //}
        }
    }
}
