package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowSlime;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;

import java.lang.reflect.Constructor;

public class SlimeStore<T extends GlowSlime> extends LivingEntityStore<T> {

    private final Constructor<T> constructor;

    public SlimeStore(Class<T> clazz, String id) {
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
    public void load(T entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setSize(compound.getInt("Size") + 1);
    }

    @Override
    public void save(T entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putInt("Size", entity.getSize() - 1);
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
}
