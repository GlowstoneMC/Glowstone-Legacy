package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowPigZombie;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;

public class PigZombieStore extends ZombieStore<GlowPigZombie> {

    public PigZombieStore() {
        super(GlowPigZombie.class, "PigZombie");
    }

    @Override
    public void load(GlowPigZombie entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setAnger(compound.getShort("Anger"));
        // TODO implement hurtBy

    }

    @Override
    public void save(GlowPigZombie entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putShort("Anger", entity.getAnger());
        // TODO implement hurtBy
    }

    @Override
    public GlowPigZombie createEntity(Location location, CompoundTag compound) {
        return new GlowPigZombie(location);
    }
}
