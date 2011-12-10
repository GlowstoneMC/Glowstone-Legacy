package net.glowstone.entity;

import net.glowstone.GlowServer;

import org.bukkit.entity.Creature;

import net.glowstone.util.Position;
import net.glowstone.msg.Message;
import net.glowstone.msg.SpawnMobMessage;
import net.glowstone.util.Parameter;
import net.glowstone.GlowWorld;
import org.bukkit.entity.LivingEntity;

/**
 * Represents a creature such as a creeper.
 * @author Graham Edgecombe
 */
public class GlowCreature extends GlowLivingEntity implements Creature {

    /**
     * The type of creature.
     */
    private final int type;

    /**
     * The creature target.
     */
    private LivingEntity target;

    /**
     * Creates a new creature.
     * @param world The world this creature is in.
     * @param type The type of creature.
     */
    public GlowCreature(GlowServer server, GlowWorld world, int type) {
        super(server, world);
        this.type = type;
    }

    /**
     * Gets the type of creature.
     * @return The type of creature.
     */
    public int getType() {
        return type;
    }

    @Override
    public Message createSpawnMessage() {
        int x = Position.getIntX(location);
        int y = Position.getIntY(location);
        int z = Position.getIntZ(location);
        int yaw = Position.getIntYaw(location);
        int pitch = Position.getIntPitch(location);
        return new SpawnMobMessage(id, type, x, y, z, yaw, pitch, metadata);
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public int getMaxHealth() {
        throw new UnsupportedOperationException("Not supported yet!");
    }

}
