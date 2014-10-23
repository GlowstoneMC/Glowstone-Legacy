package net.glowstone.generator.populators;

import net.glowstone.generator.decorators.*;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomePopulator extends BlockPopulator {

    private final List<BlockPopulator> decorators = new ArrayList<BlockPopulator>();

    public BiomePopulator() {

        // the order is important

        addDecorator(new DeadBushDecorator()
                .setDefaultAmount(0)
                .setBiomeAmount(Biome.DESERT, 2)
                .setBiomeAmount(Biome.MESA, 20)
                .setBiomeAmount(Biome.SWAMPLAND, 1)
                .setBiomeAmount(Biome.TAIGA, 1));

        addDecorator(new WaterLilyDecorator()
                .setDefaultAmount(0)
                .setBiomeAmount(Biome.SWAMPLAND, 4));

        addDecorator(new SugarCaneDecorator()
                .setDefaultAmount(10)
                .setBiomeAmount(Biome.DESERT, 60)
                .setBiomeAmount(Biome.MESA, 13)
                .setBiomeAmount(Biome.SWAMPLAND, 20));

        addDecorator(new PumpkinDecorator());
    }

    @Override
    public void populate(World world, Random random, Chunk source) {
        for (BlockPopulator decorator : decorators) {
            decorator.populate(world, random, source);
        }
    }

    private void addDecorator(BlockPopulator decorator) {
        decorators.add(decorator);
    }
}
