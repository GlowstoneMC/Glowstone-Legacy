package net.glowstone.generator.populators;

import net.glowstone.GlowChunk;
import net.glowstone.generator.structures.GlowJungleTemple;
import net.glowstone.generator.structures.GlowStructurePiece;
import net.glowstone.generator.structures.GlowWitchHut;
import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class StructurePopulator extends BlockPopulator {

    private static final int MIN_DISTANCE = 8;
    private static final int MAX_DISTANCE = 32;
    private final Map<Integer, GlowStructurePiece> structures = new HashMap<>();

    public boolean shouldGenerate(Random random, int chunkX, int chunkZ) {
        int x = chunkX < 0 ? (chunkX - MAX_DISTANCE - 1) / MAX_DISTANCE : chunkX / MAX_DISTANCE;
        int z = chunkZ < 0 ? (chunkZ - MAX_DISTANCE - 1) / MAX_DISTANCE : chunkZ / MAX_DISTANCE;
        x = x * MAX_DISTANCE + random.nextInt(MAX_DISTANCE - MIN_DISTANCE);
        z = z * MAX_DISTANCE + random.nextInt(MAX_DISTANCE - MIN_DISTANCE);
        if (x == chunkX && z == chunkZ) {
            return true;
        }
        return false;
    }

    @Override
    public void populate(World world, Random random, Chunk source) {

        if (world.canGenerateStructures()) {
            final int key = new GlowChunk.Key(source.getX(), source.getZ()).hashCode();           
            final int x = source.getX() << 4;
            final int z = source.getZ() << 4;
            if (!structures.containsKey(key)) {
                GlowStructurePiece structure;
                if (random.nextBoolean()) {
                    structure = new GlowWitchHut(random, new Location(world, x, world.getSeaLevel(), z));
                } else {
                    structure = new GlowJungleTemple(random, new Location(world, x, world.getSeaLevel(), z));
                }
                if (shouldGenerate(random, source.getX(), source.getZ()) && structure.getBoundingBox().intersectsWith(x, z, x + 15, z + 15)) {
                    structures.put(key, structure);
                }
            }

            final Iterator<Entry<Integer, GlowStructurePiece>> it = structures.entrySet().iterator();            
            while (it.hasNext()) {
                final GlowStructurePiece structure = it.next().getValue();
                if (structure.getBoundingBox().intersectsWith(x, z, x + 15, z + 15)) {
                    final BlockStateDelegate delegate = new BlockStateDelegate();
                    if (structure.generate(world, random, delegate)) {
                        delegate.updateBlockStates();
                    } else {
                        it.remove();
                    }
                }
            }
        }
    }
}
