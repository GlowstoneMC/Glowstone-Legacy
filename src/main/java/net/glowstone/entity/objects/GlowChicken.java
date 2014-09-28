package net.glowstone.entity.objects;

import com.flowpowered.networking.Message;
import net.glowstone.entity.GlowLivingEntity;
import net.glowstone.net.message.play.entity.EntityMetadataMessage;
import net.glowstone.net.message.play.entity.EntityTeleportMessage;
import net.glowstone.net.message.play.entity.EntityVelocityMessage;
import net.glowstone.net.message.play.entity.SpawnObjectMessage;
import net.glowstone.util.Position;
import org.bukkit.Location;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Chicken living entity ({@link net.glowstone.entity.GlowLivingEntity} withing the world.
 * @author Momo (momoscode and MOMOTHEREAL)
 */
public class GlowChicken extends GlowLivingEntity implements Chicken {

    /**
     * The age of the mob.
     * Set by default to 0 (breed-able adult).
     */
    private int age = 0;

    private boolean ageLock;

    /**
     * Whether the mob is a baby.
     * Set by default to false (adult).
     */
    private boolean baby;

    /**
     * Whether the mob can breed itself when the circumstances allow it.
     */
    private boolean breed = true;

    /**
     * The living entity the mob is targetting.
     */
    private LivingEntity target;

    /**
     * The type of entity this mob is.
     */
    private EntityType type = EntityType.CHICKEN;

    /**
     * Creates a mob within the specified world.
     *
     * @param location The location.
     */
    public GlowChicken(Location location) {
        super(location);
        age = 0;
        baby = false;
        breed = true;
    }

    /**
     * The age of the mob.
     * @return the age.
     */
    @Override
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the mob.
     * @param age the age value.
     */
    @Override
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public void setAgeLock(boolean lock) {
        ageLock = lock;
    }

    @Override
    public boolean getAgeLock() {
        return ageLock;
    }

    /**
     * Sets the mob to a baby stage.
     * Also sets the age to the lowest negative value for a baby (-20 minutes).
     */
    @Override
    public void setBaby() {
        age = 20 * (-20 * 60); //20 minutes before getting into adult
        baby = true;
    }

    /**
     * Sets the mob to an aldut stage.
     * Also sets the age to the lowest value for an adult, so it can breed right away.
     */
    @Override
    public void setAdult() {
        age = 0; //0 ticks of age or above means that the mob is an adult.
        baby = false;
    }

    /**
     * The stage of the mob.
     * @return true if the mob is an adult.
     */
    @Override
    public boolean isAdult() {
        return !baby;
    }

    /**
     * Checks if the mob is an adult with an age value of 0, and if the sever lets the entity breed.
     * @return true if the mob can breed.
     */
    @Override
    public boolean canBreed() {
        return (isAdult() && age == 0) && (this.breed);
    }

    /**
     * Allows or disallows the mob to breed.
     * Note: this is disregarding the stage of the mob. Even if set to true, a baby cannot breed.
     * @param breed whether the mob is allowed to breed.
     */
    @Override
    public void setBreed(boolean breed) {
        breed = true;
    }

    /**
     * Sets the living entity this mob is going to target.
     * @param target a living entity the mob will target.
     */
    @Override
    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    /**
     * The living entity the mob is currently targeting.
     * @return a living entity the mob is targeting. If set to null, the mob will stop targeting.
     */
    @Override
    public LivingEntity getTarget() {
        return target;
    }

    @Override
    public List<Message> createSpawnMessage() {
        int x = Position.getIntX(location);
        int y = Position.getIntY(location);
        int z = Position.getIntZ(location);

        int yaw = Position.getIntYaw(location);
        int pitch = Position.getIntPitch(location);

        return Arrays.asList(
                new SpawnObjectMessage(id, 2, x, y, z, pitch, yaw),
                new EntityMetadataMessage(id, metadata.getEntryList()),
                // these keep the client from assigning a random velocity
                new EntityTeleportMessage(id, x, y, z, yaw, pitch),
                new EntityVelocityMessage(id, getVelocity())
        );
    }

    /**
     * The type of entity.
     * @return the type of entity.
     */
    @Override
    public EntityType getType() {
        return type;
    }

    /**
     * Occurs every tick. Changes the age value of the mob when needed.
     */
    @Override
    public void pulse() {
        super.pulse();
        if (!isAdult())
            age++;
        if (age == 0)
            setAdult();
        if (isAdult() && age > 0)
            age--;
    }
}
