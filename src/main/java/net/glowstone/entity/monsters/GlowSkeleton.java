package net.glowstone.entity.monsters;

import net.glowstone.entity.GlowMonster;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;

public class GlowSkeleton extends GlowMonster implements Skeleton {

    private SkeletonType skeletonType = SkeletonType.NORMAL;

    public GlowSkeleton(Location location) {
        super(location, EntityType.SKELETON);
    }

    @Override
    public SkeletonType getSkeletonType() {
        return skeletonType;
    }

    @Override
    public void setSkeletonType(SkeletonType skeletonType) {
        this.skeletonType = skeletonType;
    }
}
