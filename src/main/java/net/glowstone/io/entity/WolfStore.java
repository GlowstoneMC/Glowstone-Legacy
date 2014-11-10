package net.glowstone.io.entity;

import net.glowstone.entity.passive.GlowWolf;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.DyeColor;

class WolfStore extends TameableStore<GlowWolf> {

    public static final String ANGRY_TAG = "Angry";
    public static final String COLLAR_COLOR_TAG = "CollarColor";
    private static final String SITTING_TAG = "Sitting";

    public WolfStore() {
        super(GlowWolf.class, "Wolf");
    }

    @Override
    public void load(GlowWolf entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setSitting(compound.getBool(SITTING_TAG));
        entity.setAngry(compound.getBool(ANGRY_TAG));
        if (compound.containsKey(COLLAR_COLOR_TAG)) {
            byte collarColorId = compound.getByte(COLLAR_COLOR_TAG);
            DyeColor color = DyeColor.getByDyeData(collarColorId);
            entity.setCollarColor(color);
        }
    }

    @Override
    public void save(GlowWolf entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putBool(SITTING_TAG, entity.isSitting());
        tag.putBool(ANGRY_TAG, entity.isAngry());
        if (entity.getCollarColor() != null) {
            tag.putByte(COLLAR_COLOR_TAG, entity.getCollarColor().getDyeData());
        }
    }
}
