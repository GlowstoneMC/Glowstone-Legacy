package net.glowstone;

import net.glowstone.constants.GlowBiome;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Biome;

import net.glowstone.GlowChunk.ChunkSection;

/**
 * Class representing a snapshot of a chunk.
 */
public class GlowChunkSnapshot implements ChunkSnapshot {
    
    private final int x, z;
    private final String world;
    private final long time;

    private final ChunkSection[] sections;

    private final int[] height;
    private final double[] temp, humid;
    private final byte[] biomes;

    public GlowChunkSnapshot(int x, int z, World world, ChunkSection[] sections, boolean svHeight, byte[] biomes, boolean svTemp) {
        this.x = x;
        this.z = z;
        this.world = world.getName();
        this.time = world.getFullTime();

        int numSections = sections != null ? sections.length : 0;
        this.sections = new ChunkSection[numSections];
        for (int i = 0; i < numSections; ++i) {
            if (sections[i] != null) {
                this.sections[i] = sections[i].snapshot();
            }
        }

        final int baseX = x << 4, baseZ = z << 4;
        if (svHeight) {
            height = new int[16 * 16];
            for (int xx = 0; xx < 16; ++xx) {
                for (int zz = 0; zz < 16; ++zz) {
                    height[coordToIndex(xx, zz)] = world.getHighestBlockYAt(baseX + xx, baseZ + zz);
                }
            }
        } else {
            height = null;
        }

        this.biomes = biomes;

        if (svTemp) {
            temp = new double[16 * 16];
            humid = new double[16 * 16];
            for (int xx = 0; xx < 16; ++xx) {
                for (int zz = 0; zz < 16; ++zz) {
                    temp[coordToIndex(xx, zz)] = world.getTemperature(baseX + xx, baseZ + zz);
                    humid[coordToIndex(xx, zz)] = world.getHumidity(baseX + xx, baseZ + zz);
                }
            }
        } else {
            temp = humid = null;
        }
    }

    private ChunkSection getSection(int y) {
        int idx = y >> 4;
        if (idx < 0 || idx >= sections.length) {
            return null;
        }
        return sections[idx];
    }

    /**
     * Get the ChunkSection array backing this snapshot. In general, it should not be modified.
     * @return The array of ChunkSections.
     */
    public ChunkSection[] getRawSections() {
        return sections;
    }

    public int[] getRawHeightmap() {
        return height;
    }

    public byte[] getRawBiomes() {
        return biomes;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public String getWorldName() {
        return world;
    }

    public long getCaptureFullTime() {
        return time;
    }

    public boolean isSectionEmpty(int sy) {
        return sy < 0 || sy >= sections.length || (sections[sy] == null);
    }

    public int getBlockTypeId(int x, int y, int z) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : (section.types[section.index(x, y, z)] & 0xff);
    }

    public int getBlockData(int x, int y, int z) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.metaData.get(section.index(x, y, z));
    }

    public int getBlockSkyLight(int x, int y, int z) {
        ChunkSection section = getSection(y);
        return section == null ? 15 : section.skyLight.get(section.index(x, y, z));
    }

    public int getBlockEmittedLight(int x, int y, int z) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.blockLight.get(section.index(x, y, z));
    }

    public int getHighestBlockYAt(int x, int z) {
        return height[coordToIndex(x, z)];
    }

    public Biome getBiome(int x, int z) {
        return GlowBiome.getBiome(biomes[coordToIndex(x, z)]);
    }

    public double getRawBiomeTemperature(int x, int z) {
        return temp[coordToIndex(x, z)];
    }

    public double getRawBiomeRainfall(int x, int z) {
        return humid[coordToIndex(x, z)];
    }

    private int coordToIndex(int x, int z) {
        if (x < 0 || z < 0 || x >= GlowChunk.WIDTH || z >= GlowChunk.HEIGHT)
            throw new IndexOutOfBoundsException();

        return z * GlowChunk.WIDTH + x;
    }

    public static class EmptySnapshot extends GlowChunkSnapshot {
        
        public EmptySnapshot(int x, int z, World world, boolean svBiome, boolean svTemp) {
            super(x, z, world, null, false, svBiome ? new byte[256] : null, svTemp);
        }

        @Override
        public int getBlockTypeId(int x, int y, int z) {
            return 0;
        }

        @Override
        public int getBlockData(int x, int y, int z) {
            return 0;
        }

        @Override
        public int getBlockSkyLight(int x, int y, int z) {
            return 15;
        }

        @Override
        public int getBlockEmittedLight(int x, int y, int z) {
            return 0;
        }

        @Override
        public int getHighestBlockYAt(int x, int z) {
            return 0;
        }
        
    }
    
}
