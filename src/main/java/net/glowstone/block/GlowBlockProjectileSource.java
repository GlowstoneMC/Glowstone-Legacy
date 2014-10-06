package net.glowstone.block;

import net.glowstone.block.state.GlowDispenser;
import org.bukkit.block.Block;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.util.Vector;

public class GlowBlockProjectileSource implements BlockProjectileSource {

    private final GlowDispenser dispenser;

    public GlowBlockProjectileSource(GlowDispenser dispenser) {
        this.dispenser = dispenser;
    }

    @Override
    public Block getBlock() {
        return dispenser.getBlock();
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
        return launchProjectile(projectile, null);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
        //TODO: Projectile launching
        return null;
    }
}
