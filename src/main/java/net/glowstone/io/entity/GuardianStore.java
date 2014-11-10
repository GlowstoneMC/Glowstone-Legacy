package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowGuardian;
import net.glowstone.util.nbt.CompoundTag;

public class GuardianStore extends MonsterStore<GlowGuardian> {

    public static final String ELDER_TAG = "Elder";

    public GuardianStore() {
        super(GlowGuardian.class, "Guardian");
    }

    @Override
    public void load(GlowGuardian entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setElder(compound.getBool(ELDER_TAG));
    }

    @Override
    public void save(GlowGuardian entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putBool(ELDER_TAG, entity.isElder());
    }
}
