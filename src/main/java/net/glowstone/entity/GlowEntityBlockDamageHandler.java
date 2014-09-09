package net.glowstone.entity;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class GlowEntityBlockDamageHandler {


    /**
     * The entity that takes damages.
     */
    private GlowEntity parentEntity = null;

    /**
     * The last taken damage from a cactus.
     */
    private long lastCactusDamageTick = 0;


    public GlowEntityBlockDamageHandler(GlowEntity targetEntity) {
        this.parentEntity = targetEntity;
    }

    protected void damage(double amount, EntityDamageEvent event) {
        if (!(parentEntity instanceof GlowLivingEntity)) {
            parentEntity.remove();
        } else {
            LivingEntity living = (LivingEntity) parentEntity;
            living.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                living.damage(amount, null);
            }
        }
    }

    /**
     * Check damages that needs to be applied this tick.
     */
    public void pulse() {

        Location location = parentEntity.getLocation();
        World w = location.getWorld();

        // TODO : fix x or z < 0 problems.

        double x = location.getX() % 1;
        double y = location.getY() % 1;
        double z = location.getZ() % 1;

        Block[] blocksTouching = new Block[]{null, null, new Location(w, location.getX(), location.getY() - 1, location.getZ()).getBlock()};

        if ((x > 0 && x >= 0.7) || (x < 0 && x > -0.3)) {
            blocksTouching[0] = new Location(w, location.getX() + 1, location.getY(), location.getZ()).getBlock();
        } else if ((x > 0 && x <= 0.3) || (x < 0 && x < -0.7)) { // There is a problem here if x < 0.
            blocksTouching[0] = new Location(w, location.getX() - 1, location.getY(), location.getZ()).getBlock();
        }

        if ((z > 0 && z >= 0.7) || (z < 0 && z > -0.3)) {
            blocksTouching[1] = new Location(w, location.getX(), location.getY(), location.getZ() + 1).getBlock();
        } else if ((z > 0 && z <= 0.3) || (z < 0 && z < -0.7)) { // There is a problem here if x < 0.
            blocksTouching[1] = new Location(w, location.getX(), location.getY(), location.getZ() - 1).getBlock();
        }

        if (y > 0.9) {
            blocksTouching[2] = new Location(w, location.getX(), location.getY(), location.getZ()).getBlock();
        }

        for (Block touching : blocksTouching) {
            if (touching == null) {
                continue;
            }

            if (touching.getType().equals(Material.CACTUS)) {
                // Cactus //
                if (lastCactusDamageTick + 10 < parentEntity.getWorld().getWorldAge()) {
                    EntityDamageByBlockEvent ev = new EntityDamageByBlockEvent(touching, parentEntity, EntityDamageEvent.DamageCause.CONTACT, 1.0);
                    damage(1.0, ev);
                    lastCactusDamageTick = parentEntity.getWorld().getWorldAge();
                }
            }
        }
    }

}
