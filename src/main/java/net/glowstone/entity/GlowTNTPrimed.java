package net.glowstone.entity;

import com.flowpowered.networking.Message;
import net.glowstone.EventFactory;
import net.glowstone.Explosion;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import net.glowstone.util.Position;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.LinkedList;
import java.util.List;


public class GlowTNTPrimed extends GlowExplosive implements TNTPrimed {
    private int fuseTicks;
    private Entity source;

    public GlowTNTPrimed(Location location, Entity source) {
        super(location, Explosion.POWER_TNT);
        this.fuseTicks = 80;
        this.source = source;
    }

    @Override
    public void pulse() {
        super.pulse();

        fuseTicks--;
        if (fuseTicks <= 0) {
            explode();
        } else {
            world.playEffect(location.clone().add(0, 0.5d, 0), Effect.SMOKE, 0);
        }
    }

    private void explode() {
        ExplosionPrimeEvent event = EventFactory.callEvent(new ExplosionPrimeEvent(this));

        if (!event.isCancelled()) {
            Location location = getLocation();
            double x = location.getX(), y = location.getY(), z = location.getZ();
            this.getWorld().createExplosion(this, x, y, z, event.getRadius(), event.getFire(), true);
        }

        this.remove();
    }

    @Override
    public List<Message> createSpawnMessage() {
        int x = Position.getIntX(location),
                y = Position.getIntY(location),
                z = Position.getIntZ(location),
                pitch = Position.getIntPitch(location),
                yaw = Position.getIntYaw(location);

        LinkedList<Message> result = new LinkedList<>();
        result.add(new SpawnObjectMessage(id, 50, x, y, z, pitch, yaw));
        return result;
    }

    @Override
    public final void setFuseTicks(int i) {
        this.fuseTicks = i;
    }

    @Override
    public final int getFuseTicks() {
        return fuseTicks;
    }

    @Override
    public final Entity getSource() {
        return source.isValid() ? source : null;
    }

    @Override
    public final EntityType getType() {
        return EntityType.PRIMED_TNT;
    }
}
