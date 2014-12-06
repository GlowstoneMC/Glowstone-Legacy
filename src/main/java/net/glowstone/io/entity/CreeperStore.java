package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowCreeper;
import net.glowstone.util.nbt.CompoundTag;

public class CreeperStore extends MonsterStore<GlowCreeper> {

    public CreeperStore() {
        super(GlowCreeper.class, "Creeper");
    }

    @Override
    public void load(GlowCreeper entity, CompoundTag compound) {
        super.load(entity, compound);
        if (compound.containsKey("powered")) {
            entity.setPowered(compound.getBool("powered"));
        }
        if (compound.containsKey("Fuse")) {
            entity.setFuse(compound.getShort("Fuse"));
        }
        if (compound.containsKey("ExplosionRadius")) {
            entity.setExplosionRadius(compound.getByte("ExplosionRadius"));
        }
        if (compound.containsKey("Ignited")) {
            entity.setIgnited(compound.getBool("ignited"));
        }
    }

    @Override
    public void save(GlowCreeper entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putBool("powered", entity.isPowered());
        /* TODO add necessary hooks
        tag.putShort("Fuse", entity.getFuse());
        tag.putByte("ExplosionRadius", entity.getExplosionRadius());
        tag.putBoolean("ignited", entity.isIgnited());
        */
    }
}
