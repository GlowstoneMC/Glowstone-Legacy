package net.glowstone.io.entity;

import net.glowstone.entity.passive.GlowSquid;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;

class SquidStore extends LivingEntityStore<GlowSquid> {

    public SquidStore() {
        super(GlowSquid.class, "Squid");
    }

    public void load(GlowSquid entity, CompoundTag compound) {
        super.load(entity, compound);

    }

    public void save(GlowSquid entity, CompoundTag tag) {
        super.save(entity, tag);
    }

    @Override
    public GlowSquid createEntity(Location location, CompoundTag compound) {
        return new GlowSquid(location);
    }
}
