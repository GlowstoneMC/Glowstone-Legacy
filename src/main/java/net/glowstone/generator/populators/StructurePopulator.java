package net.glowstone.generator.populators;

import net.glowstone.GlowChunk;
import net.glowstone.GlowWorld;
import net.glowstone.generator.structures.GlowStructure;
import net.glowstone.io.structure.StructureStorage;
import net.glowstone.io.structure.StructureStore;
import net.glowstone.util.BlockStateDelegate;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class StructurePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk source) {

        if (world.canGenerateStructures()) {
            final Map<Integer, GlowStructure> structures = ((GlowWorld) world).getStructures();
            final int key = new GlowChunk.Key(source.getX(), source.getZ()).hashCode();           
            final int x = source.getX() << 4;
            final int z = source.getZ() << 4;
            if (!structures.containsKey(key)) {
                for (StructureStore<?> store : StructureStorage.getStructureStores()) {
                    final GlowStructure structure = store.createNewStructure((GlowWorld) world, random, source.getX(), source.getZ());
                    if (structure.shouldGenerate(random) && structure.getBoundingBox().intersectsWith(x, z, x + 15, z + 15)) {
                        structure.setDirty(true);
                        structures.put(key, structure);
                    }
                }
            }

            final Iterator<Entry<Integer, GlowStructure>> it = ((GlowWorld) world).getStructures().entrySet().iterator();            
            while (it.hasNext()) {
                final GlowStructure structure = it.next().getValue();
                if (structure.getBoundingBox().intersectsWith(x, z, x + 15, z + 15)) {
                    final BlockStateDelegate delegate = new BlockStateDelegate();
                    if (structure.generate(random, delegate)) { // maybe later trigger a StructureGeneratedEvent event and cancel
                        delegate.updateBlockStates();
                    } else {
                        it.remove();
                    }
                }
            }
        }
    }
}
