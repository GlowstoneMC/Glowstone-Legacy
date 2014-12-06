package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowGhast;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;

public class GhastStore extends LivingEntityStore<GlowGhast> {

    public GhastStore() {
        super(GlowGhast.class, "Ghast");
    }

    @Override
    public GlowGhast createEntity(Location location, CompoundTag compound) {
        return new GlowGhast(location);
    }

    @Override
    public void load(GlowGhast entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setExplosionPower(compound.getInt("ExplosionPower"));
    }

    @Override
    public void save(GlowGhast entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putInt("ExplosionPower", entity.getExplosionPower());
    }
}
