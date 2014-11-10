package net.glowstone.io.entity;

import net.glowstone.entity.passive.GlowSnowman;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;

class SnowmanStore extends CreatureStore<GlowSnowman> {

    public SnowmanStore() {
        super(GlowSnowman.class, "Snowman");
    }

    public void load(GlowSnowman entity, CompoundTag compound) {
        super.load(entity, compound);

    }

    public void save(GlowSnowman entity, CompoundTag tag) {
        super.save(entity, tag);
    }

    @Override
    public GlowSnowman createEntity(Location location, CompoundTag compound) {
        return new GlowSnowman(location);
    }
}
