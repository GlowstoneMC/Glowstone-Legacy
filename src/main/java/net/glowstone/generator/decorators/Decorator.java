package net.glowstone.generator.decorators;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class Decorator {

    private int defaultAmount;
    private final Map<Biome, Integer> biomesDecorations = new HashMap<Biome, Integer>();

    protected final int getAmountAt(World world, Chunk chunk) {
        final Biome biome = world.getBiome(chunk.getX() << 4, chunk.getZ() << 4); 
        if (biomesDecorations.containsKey(biome)) {
            return biomesDecorations.get(biome);
        }
        return defaultAmount;
    }

    public final Decorator setDefaultAmount(int amount) {
        defaultAmount = amount;
        return this;
    }

    public final Decorator setBiomeAmount(Biome biome, int amount) {
        biomesDecorations.put(biome, amount);
        return this;
    }

    public void decorate(World world, Random random, Chunk source) {
        // do nothing
    }
}
