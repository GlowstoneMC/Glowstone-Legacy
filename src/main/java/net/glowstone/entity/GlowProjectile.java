package net.glowstone.entity;

import com.flowpowered.networking.Message;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;

public class GlowProjectile extends GlowEntity implements Projectile {

    private final EntityType type;

    private boolean doesBounce = true;

    private ProjectileSource shooter;

    public GlowProjectile(Location location, EntityType type) {
        super(location);
        this.type = type;
    }

    public GlowProjectile(Location location, EntityType type, ProjectileSource shooter) {
        super(location);
        this.type = type;
        this.shooter = shooter;
    }

    @Override
    public List<Message> createSpawnMessage() {
        return new ArrayList<>();
    }

    @Override
    public LivingEntity _INVALID_getShooter() {
        if (shooter instanceof LivingEntity) {
            return (LivingEntity) shooter;
        } else {
            return null;
        }
    }

    @Override
    public ProjectileSource getShooter() {
        return shooter;
    }

    @Override
    public void setShooter(ProjectileSource projectileSource) {
        this.shooter = projectileSource;
    }

    @Override
    public void _INVALID_setShooter(LivingEntity entity) {
        this.shooter = entity;
    }

    @Override
    public boolean doesBounce() {
        return doesBounce;
    }

    @Override
    public void setBounce(boolean doesBounce) {
        this.doesBounce = doesBounce;
    }

    @Override
    public EntityType getType() {
        return type;
    }
}
