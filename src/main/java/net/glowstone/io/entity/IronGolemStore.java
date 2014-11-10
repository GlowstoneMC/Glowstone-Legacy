package net.glowstone.io.entity;

import net.glowstone.entity.passive.GlowIronGolem;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.Location;

class IronGolemStore extends CreatureStore<GlowIronGolem> {

    private static final String PLAYER_CREATED_TAG = "PlayerCreated";

    public IronGolemStore() {
        super(GlowIronGolem.class, "VillagerGolem");
    }

    public void load(GlowIronGolem entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setPlayerCreated(compound.getBool(PLAYER_CREATED_TAG));

    }

    public void save(GlowIronGolem entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putBool(PLAYER_CREATED_TAG, entity.isPlayerCreated());
    }

    @Override
    @SuppressWarnings("unchecked")
    public GlowIronGolem createEntity(Location location, CompoundTag compound) {
        return new GlowIronGolem(location);
    }
}
