package net.glowstone.generator;

import net.glowstone.generator.populators.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.Map;
import java.util.Random;

/**
 * Basic generator with lots of hills.
 */
public class SurfaceGenerator extends GlowChunkGenerator {

    public SurfaceGenerator() {
        super(
                // In-ground
                new LakePopulator(),
                // On-ground
                // Desert is before tree and mushroom but snow is after so trees have snow on top
                new DesertPopulator(),
                //new TreePopulator(),
                new MushroomPopulator(),
                new SnowPopulator(),
                new FlowerPopulator(),
                new BiomePopulator(),
                // Below-ground
                new DungeonPopulator(),
                //new CavePopulator(),
                new OrePopulator()
        );
    }

    private OctaveGenerator height;

    private OctaveGenerator density;
    private OctaveGenerator roughness;
    private OctaveGenerator detail;

    @Override
    public byte[] generate(World world, Random random, int chunkX, int chunkZ) {
        /*if (density == null)*/ createWorldOctaves(world, null);

        chunkX <<= 4;
        chunkZ <<= 4;

        boolean nether = world.getEnvironment() == Environment.NETHER;
        Material matMain = nether ? Material.NETHERRACK : Material.DIRT;
        Material matShore = nether ? Material.SOUL_SAND : Material.SAND;
        Material matShore2 = Material.GRAVEL;
        Material matTop = nether ? Material.NETHERRACK : Material.GRASS;
        Material matUnder = nether ? Material.NETHERRACK : Material.STONE;
        Material matLiquid = nether ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER;

        byte[] buf = start(Material.AIR);

        int baseHeight = WORLD_DEPTH / 2;
        double terrainHeight = 50;
        boolean noDirt = true;
        int waterLevel = WORLD_DEPTH / 2;

        for (int ix = 0; ix < 16; ix++) {
            for (int iz = 0; iz < 16; iz++) {
                int deep = 0;
                int x = ix + chunkX;
                int z = iz + chunkZ;

                // height is sea or noise
                double h = height.noise(x, z, 2.5, 0.9, true);
                if (h > 0) {
                    int top = 0;
                    for (int y = 127; y > 0; y--) {
                        double finalDensity;
                        if (h < 0.15) {
                            // linear interpolation
                            finalDensity =
                                    (h * (20 / 3.0)) * grasslandDensity(x, y, z) +
                                    (1 - (20 / 3.0) * h) * seaDensity(x, y, z);
                        } else {
                            finalDensity = grasslandDensity(x, y, z);
                        }
                        if (finalDensity > 0) {
                            if (y > top) top = y;

                            if (y == top) {
                                if (y <= 60) {
                                    set(buf, ix, y, iz, Material.DIRT);
                                } else {
                                    if (h < 0.15) {
                                        set(buf, ix, y, iz, Material.SAND);
                                    } else {
                                        set(buf, ix, y, iz, Material.GRASS);
                                    }
                                }
                            } else if (y > top - 4) {
                                set(buf, ix, y, iz, Material.DIRT);
                            } else {
                                set(buf, ix, y, iz, Material.STONE);
                            }
                        } else if (y <= 60) {
                            set(buf, ix, y, iz, Material.WATER);
                        }
                    }
                } else {
                    int top = 0;
                    for (int y = 60; y > 0; y--) {
                        double finalDensity = seaDensity(x, y, z);
                        if (finalDensity > 0) {
                            if (y > top) top = y;

                            if (y == top) {
                                set(buf, ix, y, iz, Material.GRAVEL);
                            } else if (y > top - 4) {
                                set(buf, ix, y, iz, Material.DIRT);
                            } else {
                                set(buf, ix, y, iz, Material.STONE);
                            }
                        } else {
                            set(buf, ix, y, iz, Material.WATER);
                        }
                    }
                }

                set(buf, ix, 0, iz, Material.BEDROCK);
            }
        }

        return buf;
    }

    private double grasslandDensity(int x, int y, int z) {
        return density.noise(x, y, z, 1.2, 0.6, true)
                - roughness.noise(x, y, z, 1.2, 0.6, true)
                * detail.noise(x, y, z, 1.2, 0.6, true) + 50.0 / 3 - (5.0 / 24) * y;
    }

    private double seaDensity(int x, int y, int z) {
        return density.noise(x, y, z, 1.2, 0.6, true)
                - roughness.noise(x, y, z, 1.2, 0.6, true)
                * detail.noise(x, y, z, 1.2, 0.6, true) + (54 - y) / 3;
    }

    @Override
    protected void createWorldOctaves(World world, Map<String, OctaveGenerator> octaves) {
        Random seed = new Random(world.getSeed());
        double largeScale = 1 / 8.0;

        /* With default settings, this is 5 octaves. With tscale=256,terrainheight=50,
         * this comes out to 14 octaves, which makes more complex terrain at the cost
         * of more complex generation. Without this, the terrain looks bad, especially
         * on higher tscale/terrainheight pairs. */
        /*double value = Math.round(Math.sqrt(50 * 256.0 / (128 - 50)) * 1.1 - 0.2);
        OctaveGenerator gen = new SimplexOctaveGenerator(seed, Math.max((int) value, 5));
        gen.setScale(1 / 256.0);
        octaves.put("height", gen);

        gen = new SimplexOctaveGenerator(seed, gen.getOctaves().length / 2);
        gen.setScale(Math.min(256.0 / 1024, 1 / 32.0));
        octaves.put("jitter", gen);

        gen = new SimplexOctaveGenerator(seed, 2);
        gen.setScale(1 / WORLD_DEPTH);
        octaves.put("type", gen);


        octaves.put("density", gen);*/

        // we don't care about y for the height so we set the scale overall
        height = new SimplexOctaveGenerator(seed, 5);
        height.setScale(largeScale / 32 / 8);

        density = new SimplexOctaveGenerator(seed, 1);
        density.setXScale(largeScale / 1 / 8);
        density.setYScale(largeScale / 1 / 4);
        density.setZScale(largeScale / 1 / 8);

        roughness = new SimplexOctaveGenerator(seed, 1);
        roughness.setXScale(largeScale / 1 / 8);
        roughness.setYScale(largeScale / 1 / 4);
        roughness.setZScale(largeScale / 1 / 8);

        detail = new SimplexOctaveGenerator(seed, 1);
        detail.setXScale(largeScale * 3 / 8);
        detail.setYScale(largeScale * 3 / 4);
        detail.setZScale(largeScale * 3 / 8);
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        return new Location(world, 0, 128, 0);
    }

}
