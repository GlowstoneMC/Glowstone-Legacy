package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowZombie;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;

import java.lang.reflect.Constructor;

class ZombieStore<T extends GlowZombie> extends CreatureStore<T> {

    private static final String BREAK_DOORS_TAG = "CanBreakDoors";
    private static final String CONVERSION_TIME_TAG = "ConversionTime";
    private static final String VILLAGER_TAG = "IsVillager";
    private static final String BABY_TAG = "IsBaby";
    private final Constructor<T> constructor;

    public ZombieStore(Class<T> clazz, String id) {
        super(clazz, id);
        Constructor<T> ctor = null;
        try {
            ctor = clazz.getConstructor(Location.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.constructor = ctor;
    }

    public void load(T entity, CompoundTag compound) {
        super.load(entity, compound);
        if (compound.containsKey(BABY_TAG)) {
            entity.setBaby(compound.getBool(BABY_TAG));
        }
        if (compound.containsKey(VILLAGER_TAG)) {
            entity.setVillager(compound.getBool(VILLAGER_TAG));
        }
        entity.setConversionTime(compound.getInt(CONVERSION_TIME_TAG));
        entity.setCanBreakDoors(compound.getBool(BREAK_DOORS_TAG));
    }

    public void save(T entity, CompoundTag tag) {
        super.save(entity, tag);
        if (entity.isBaby()) {
            tag.putBool(BABY_TAG, entity.isBaby());
        }
        if (entity.isVillager()) {
            tag.putBool(VILLAGER_TAG, entity.isVillager());
        }
        tag.putInt(CONVERSION_TIME_TAG, entity.getConversionTime());
        tag.putBool(BREAK_DOORS_TAG, entity.canBreakDoors());
    }

    @Override
    @SuppressWarnings("unchecked")
    public T createEntity(Location location, CompoundTag compound) {
        try {
            return constructor.newInstance(location);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
