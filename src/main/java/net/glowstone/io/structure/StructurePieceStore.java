package net.glowstone.io.structure;

import org.bukkit.util.Vector;

import net.glowstone.generator.structures.GlowStructurePiece;
import net.glowstone.generator.structures.util.StructureBoundingBox;
import net.glowstone.util.nbt.CompoundTag;

public abstract class StructurePieceStore<T extends GlowStructurePiece> {
    private final String id;
    private final Class<T> clazz;

    public StructurePieceStore(Class<T> clazz, String id) {
        this.id = id;
        this.clazz = clazz;
    }

    public final String getId() {
        return id;
    }

    public final Class<T> getType() {
        return clazz;
    }

    public abstract T createStructurePiece();

    public void load(T structurePiece, CompoundTag compound) {
        if (compound.isInt("GD")) {
            structurePiece.setGD(compound.getInt("GD"));
        }
        if (compound.isInt("O")) {
            structurePiece.setNumericOrientation(compound.getInt("O"));
        }
        if (compound.isIntArray("BB")) {
            int[] bb = compound.getIntArray("BB");
            if (bb.length == 6) {
                final StructureBoundingBox boundingBox = new StructureBoundingBox(new Vector(bb[0], bb[1], bb[2]), new Vector(bb[3], bb[4], bb[5]));
                structurePiece.setBoundingBox(boundingBox);
            }
        }
    }

    public void save(T structurePiece, CompoundTag compound) {
        compound.putInt("GD", structurePiece.getGD());
        compound.putInt("O", structurePiece.getNumericOrientation());
        StructureBoundingBox boundingBox = structurePiece.getBoundingBox();
        int[] bb = new int[6];
        bb[0] = boundingBox.getMin().getBlockX();
        bb[1] = boundingBox.getMin().getBlockY();
        bb[2] = boundingBox.getMin().getBlockZ();
        bb[3] = boundingBox.getMax().getBlockX();
        bb[4] = boundingBox.getMax().getBlockY();
        bb[5] = boundingBox.getMax().getBlockZ();
        compound.putIntArray("BB", bb);
    }
}
