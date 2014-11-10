package net.glowstone.io.entity;

import net.glowstone.entity.passive.GlowOcelot;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.entity.Ocelot;

class OcelotStore extends TameableStore<GlowOcelot> {

    private static final String CAT_TYPE_TAG = "CatType";
    private static final String SITTING_TAG = "Sitting";

    public OcelotStore() {
        super(GlowOcelot.class, "Ozelot");
    }

    @Override
    public void load(GlowOcelot entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setSitting(compound.getBool(SITTING_TAG));
        entity.setCatType(Ocelot.Type.getType(compound.getInt(CAT_TYPE_TAG)));
    }

    @Override
    public void save(GlowOcelot entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putBool(SITTING_TAG, entity.isSitting());
        tag.putInt(CAT_TYPE_TAG, entity.getCatType().getId());
    }
}
