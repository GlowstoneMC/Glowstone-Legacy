package net.glowstone.util;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Sound;

/**
 * A class that encapsulates relevant data for playing sounds (volume and pitch).
 */
public class GlowSound {
    private static final Random random = new Random();

    private final Sound sound;
    private final float volume;
    private final float pitch;

    /**
     * Constructs a new GlowSound with the given sound and a volume and pitch of 1
     * @param sound The Bukkit sound enum constant
     */
    public GlowSound(Sound sound) {
        this(sound, 1F, 1F);
    }

    /**
     * Constructs a new GlowSound with the given sound, volume and pitch
     * @param sound The Bukkit sound enum constant
     * @param volume Volume of sound
     * @param pitch Pitch of sound
     */
    public GlowSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Plays the sound at the given location with a slight variation in volume.
     * @param location Location of the block producing this sound
     */
    public void play(Location location) {
        // Sound pitch has a random deviation of +/- 0.25.
        location.getWorld().playSound(location, sound, volume, pitch + random.nextFloat() * 0.5F - 0.25F);
    }
}