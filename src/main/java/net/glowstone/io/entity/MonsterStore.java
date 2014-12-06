package net.glowstone.io.entity;

import net.glowstone.entity.GlowMonster;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;

import java.lang.reflect.Constructor;

class MonsterStore<T extends GlowMonster> extends CreatureStore<T> {

    private final Constructor<T> constructor;

    public MonsterStore(Class<T> clazz, String id) {
        super(clazz, id);
        Constructor<T> ctor = null;
        try {
            ctor = clazz.getConstructor(Location.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.constructor = ctor;
    }

    @Override
    public T createEntity(Location location, CompoundTag compound) {
        try {
            return constructor.newInstance(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void load(T entity, CompoundTag compound) {
        super.load(entity, compound);
    }

    public void save(T entity, CompoundTag tag) {
        super.save(entity, tag);
    }
}
