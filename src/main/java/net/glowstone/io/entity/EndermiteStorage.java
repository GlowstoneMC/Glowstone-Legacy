package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowEndermite;
import net.glowstone.util.nbt.CompoundTag;

public class EndermiteStorage extends MonsterStore<GlowEndermite> {

    public EndermiteStorage() {
        super(GlowEndermite.class, "Endermite");
    }

    @Override
    public void load(GlowEndermite entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setLifetime(compound.getInt("Lifetime"));
        // TODO entity.setPlayerSpawned(compound.getBool("PlayerSpawned"));
    }

    @Override
    public void save(GlowEndermite entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putInt("Lifetime", entity.getLifetime());
        // TODO tag.putBool("PlayerSpawned", entity.isPlayerSpawned());
    }
}
