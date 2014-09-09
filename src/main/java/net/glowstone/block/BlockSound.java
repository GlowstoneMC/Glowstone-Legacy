package net.glowstone.block;

import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.block.Block;

public class BlockSound {
    private static final Random random = new Random();

    private final Sound sound;
    private final float volume;
    private final float pitch;

    public BlockSound(Sound sound) {
        this(sound, 1F, 1F);
    }

    public BlockSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Plays the block's sound at the given location.
     * @param location Location of the block producing this sound
     */
    public void play(Block block) {
        // Sound pitch has a random deviation of +/- 0.25.
        block.getWorld().playSound(block.getLocation(), sound, volume, pitch + random.nextFloat() * 0.5F - 0.25F);
    }
}