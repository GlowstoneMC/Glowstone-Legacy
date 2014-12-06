package net.glowstone.io.entity;

import net.glowstone.entity.monsters.GlowSkeleton;
import net.glowstone.util.nbt.CompoundTag;
import org.bukkit.entity.Skeleton;

public class SkeletonStore extends MonsterStore<GlowSkeleton> {

    public SkeletonStore() {
        super(GlowSkeleton.class, "Skeleton");
    }

    @Override
    public void load(GlowSkeleton entity, CompoundTag compound) {
        super.load(entity, compound);
        entity.setSkeletonType(Skeleton.SkeletonType.getType(compound.getByte("SkeletonType")));
    }

    @Override
    public void save(GlowSkeleton entity, CompoundTag tag) {
        super.save(entity, tag);
        tag.putByte("SkeletonType", entity.getSkeletonType().getId());
    }
}
