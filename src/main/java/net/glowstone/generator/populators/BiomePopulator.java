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

        addDecorator(new DesertWellDecorator()
                .setDefaultAmount(0)
                .setBiomeAmount(5, Biome.OCEAN) // fix for lack of biomes
                .setBiomeAmount(1, Biome.DESERT, Biome.DESERT_HILLS));

        addDecorator(new DeadBushDecorator()
                .setDefaultAmount(0)
                .setBiomeAmount(1, Biome.OCEAN) // fix for lack of biomes
                .setBiomeAmount(2, Biome.DESERT, Biome.DESERT_HILLS)
                .setBiomeAmount(20, Biome.MESA, Biome.MESA_PLATEAU, Biome.MESA_PLATEAU_FOREST)
                .setBiomeAmount(1, Biome.SWAMPLAND)
                .setBiomeAmount(1, Biome.TAIGA));

        addDecorator(new WaterLilyDecorator()
                .setDefaultAmount(0)
                .setBiomeAmount(4, Biome.SWAMPLAND));

        addDecorator(new SugarCaneDecorator()
                .setDefaultAmount(10)
                .setBiomeAmount(60, Biome.DESERT, Biome.DESERT_HILLS)
                .setBiomeAmount(13, Biome.MESA, Biome.MESA_PLATEAU, Biome.MESA_PLATEAU_FOREST)
                .setBiomeAmount(20, Biome.SWAMPLAND));

        addDecorator(new MelonDecorator()
                .setDefaultAmount(0)
                .setBiomeAmount(1, Biome.OCEAN) // fix for lack of biomes
                .setBiomeAmount(1, Biome.JUNGLE, Biome.JUNGLE_HILLS, Biome.JUNGLE_EDGE));

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
