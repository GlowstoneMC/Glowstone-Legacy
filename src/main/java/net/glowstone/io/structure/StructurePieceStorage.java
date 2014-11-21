package net.glowstone.io.structure;

import java.util.HashMap;
import java.util.Map;

import net.glowstone.generator.structures.GlowStructurePiece;
import net.glowstone.util.nbt.CompoundTag;

public final class StructurePieceStorage {

    private StructurePieceStorage() {
    }

    private static final Map<String, StructurePieceStore<?>> idTable = new HashMap<>();
    private static final Map<Class<? extends GlowStructurePiece>, StructurePieceStore<?>> classTable = new HashMap<>();

    static {
        bind(new DesertTempleStore());
        bind(new JungleTempleStore());
        bind(new WitchHutStore());
    }

    private static <T extends GlowStructurePiece> void bind(StructurePieceStore<T> store) {
        idTable.put(store.getId(), store);
        classTable.put(store.getType(), store);
    }

    public static GlowStructurePiece loadStructurePiece(CompoundTag compound) {
        // look up the store by the tag's id
        if (!compound.isString("id")) {
            throw new IllegalArgumentException("StructurePiece has no type");
        }
        StructurePieceStore<?> store = idTable.get(compound.getString("id"));
        if (store == null) {
            throw new IllegalArgumentException("Unknown structure piece type to load: \"" + compound.getString("id") + "\"");
        }

        return createStructurePiece(store, compound);
    }

    public static void saveStructurePiece(GlowStructurePiece structurePiece, CompoundTag compound) {
        // look up the store for the structure piece
        StructurePieceStore<?> store = classTable.get(structurePiece.getClass());
        if (store == null) {
            throw new IllegalArgumentException("Unknown structure piece type to save: \"" + structurePiece.getClass() + "\"");
        }

        compound.putString("id", store.getId());

        getBaseStore(store).save(structurePiece, compound);
    }

    private static <T extends GlowStructurePiece> T createStructurePiece(StructurePieceStore<T> store, CompoundTag compound) {
        T structurePiece = store.createStructurePiece();
        store.load(structurePiece, compound);
        return structurePiece;
    }

    /**
     * Unsafe-cast an unknown StructurePieceStore to the base type.
     */
    @SuppressWarnings("unchecked")
    private static StructurePieceStore<GlowStructurePiece> getBaseStore(StructurePieceStore<?> store) {
        return ((StructurePieceStore<GlowStructurePiece>) store);
    }
}
