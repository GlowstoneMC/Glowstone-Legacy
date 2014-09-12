package net.glowstone.entity;

import com.flowpowered.networking.Message;
import net.glowstone.EventFactory;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import net.glowstone.util.Position;
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
        super(location, 80);
        this.source = source;
    }

    @Override
    public void pulse() {
        super.pulse();

        fuseTicks--;
        if (fuseTicks <= 0) {
            explode();
        }
    }

    private void explode() {
        ExplosionPrimeEvent event = EventFactory.onExplosionPrime(this);

        if (!event.isCancelled()) {
            Location location = getLocation();
            double x = location.getX(), y = location.getY(), z = location.getZ();
            this.getWorld().createExplosion(this, x, y, z, 4f, isIncendiary(), true);
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
    public void setFuseTicks(int i) {
        this.fuseTicks = i;
    }

    @Override
    public int getFuseTicks() {
        return fuseTicks;
    }

    @Override
    public Entity getSource() {
        return source.isValid() ? source : null;
    }

    @Override
    public EntityType getType() {
        return EntityType.PRIMED_TNT;
    }
}
