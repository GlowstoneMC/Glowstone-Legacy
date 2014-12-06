package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowWither;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;

public class WitherStore extends MonsterStore<GlowWither> {

    public WitherStore() {
        super(GlowWither.class, "Wither");
    }

    @Override
    public GlowWither createEntity(Location location, CompoundTag compound) {
        return new GlowWither(location);
    }

    @Override
    public void load(GlowWither entity, CompoundTag compound) {
        super.load(entity, compound);
        // TODO entity invulnerability time
    }

    @Override
    public void save(GlowWither entity, CompoundTag tag) {
        super.save(entity, tag);
        // TODO entity invulnerability time
    }
}
