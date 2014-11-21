package net.glowstone.io.structure;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.glowstone.GlowWorld;
import net.glowstone.generator.structures.GlowStructure;
import net.glowstone.util.nbt.CompoundTag;

public final class StructureStorage {

    private StructureStorage() {
    }

    private static final Map<String, StructureStore<?>> idTable = new HashMap<>();
    private static final Map<Class<? extends GlowStructure>, StructureStore<?>> classTable = new HashMap<>();

    static {
        bind(new TempleStore());
        //bind(new VillageStore());
        //bind(new StrongholdStore());
        //bind(new MineshaftStore());
    }

    private static <T extends GlowStructure> void bind(StructureStore<T> store) {
        idTable.put(store.getId(), store);
        classTable.put(store.getType(), store);
    }

    public static Collection<StructureStore<?>> getStructureStores() {
        return idTable.values();
    }

    public static GlowStructure loadStructure(GlowWorld world, CompoundTag compound) {
        // look up the store by the tag's id
        if (!compound.isString("id")) {
            throw new IllegalArgumentException("Structure has no type");
        }
        StructureStore<?> store = idTable.get(compound.getString("id"));
        if (store == null) {
            throw new IllegalArgumentException("Unknown structure type: \"" + compound.getString("id") + "\"");
        }

        int x = 0, z = 0;
        if (compound.isInt("ChunkX")) {
            x = compound.getInt("ChunkX");
        } else {
            throw new IllegalArgumentException("Unknown structure tag doesn't have ChunkX");
        }
        if (compound.isInt("ChunkZ")) {
            z = compound.getInt("ChunkZ");
        }

        return createStructure(world, x, z, store, compound);
    }

    public static StructureStore<GlowStructure> saveStructure(GlowStructure structure, CompoundTag compound) {
        // look up the store for the structure
        StructureStore<?> store = classTable.get(structure.getClass());
        if (store == null) {
            throw new IllegalArgumentException("Unknown structure type to save: \"" + structure.getClass() + "\"");
        }

        compound.putString("id", store.getId());
        compound.putInt("ChunkX", structure.getChunkX());
        compound.putInt("ChunkZ", structure.getChunkZ());

        StructureStore<GlowStructure> baseStore = getBaseStore(store);
        baseStore.save(structure, compound);

        return baseStore;
    }

    private static <T extends GlowStructure> T createStructure(GlowWorld world, int chunkX, int chunkZ, StructureStore<T> store, CompoundTag compound) {
        T structure = store.createStructure(world, chunkX, chunkZ);
        store.load(structure, compound);
        return structure;
    }

    /**
     * Unsafe-cast an unknown StructureStore to the base type.
     */
    @SuppressWarnings("unchecked")
    private static StructureStore<GlowStructure> getBaseStore(StructureStore<?> store) {
        return ((StructureStore<GlowStructure>) store);
    }
}
