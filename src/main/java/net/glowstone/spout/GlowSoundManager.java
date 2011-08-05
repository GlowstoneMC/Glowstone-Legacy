package net.glowstone.spout;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import org.getspout.spoutapi.event.sound.BackgroundMusicEvent;
import org.getspout.spoutapi.packet.PacketDownloadMusic;
import org.getspout.spoutapi.packet.PacketPlaySound;
import org.getspout.spoutapi.packet.PacketStopMusic;
import org.getspout.spoutapi.player.SpoutPlayer;
import org.getspout.spoutapi.sound.Music;
import org.getspout.spoutapi.sound.SoundEffect;
import org.getspout.spoutapi.sound.SoundManager;
import org.getspout.spoutapi.SpoutManager;

/**
 * Sound manager for Spout integration. Modified SimpleSoundManager from the
 * Spout plugin for CraftBukkit, used with permission.
 */
public class GlowSoundManager implements SoundManager {

    // sound effects
    
    public void playGlobalSoundEffect(SoundEffect effect) {
        playGlobalSoundEffect(effect, null);
    }

    public void playGlobalSoundEffect(SoundEffect effect, Location location) {
        playGlobalSoundEffect(effect, location, 16);
    }

    public void playGlobalSoundEffect(SoundEffect effect, Location location, int distance) {
        playGlobalSoundEffect(effect, location, distance, 16);
    }

    public void playGlobalSoundEffect(SoundEffect effect, Location location, int distance, int volumePercent) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playSoundEffect(SpoutManager.getPlayer(player), effect, location, distance, volumePercent);
        }
    }

    public void playSoundEffect(SpoutPlayer target, SoundEffect effect) {
        playSoundEffect(target, effect, null);
    }

    public void playSoundEffect(SpoutPlayer target, SoundEffect effect, Location location) {
        playSoundEffect(target, effect, location, 16);
    }

    public void playSoundEffect(SpoutPlayer target, SoundEffect effect, Location location, int distance) {
        playSoundEffect(target, effect, location, distance, 16);
    }

    public void playSoundEffect(SpoutPlayer target, SoundEffect effect, Location location, int distance, int volumePercent) {
        if (target.isSpoutCraftEnabled()) {
            if (location == null || target.getWorld().equals(location.getWorld())) {
                if (location == null) {
                    target.sendPacket(new PacketPlaySound(effect, distance, volumePercent));
                } else {
                    target.sendPacket(new PacketPlaySound(effect, location, distance, volumePercent));
                }
            }
        }
    }

    // music
    
    public void playGlobalMusic(Music music) {
        playGlobalMusic(music, 100);
    }

    public void playGlobalMusic(Music music, int volumePercent) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playMusic(SpoutManager.getPlayer(player), music, volumePercent);
        }
    }

    public void playMusic(SpoutPlayer target, Music music) {
        playMusic(target, music, 100);
    }

    public void playMusic(SpoutPlayer target, Music music, int volumePercent) {
        if (target.isSpoutCraftEnabled()) {
            BackgroundMusicEvent event = new BackgroundMusicEvent(music, volumePercent, target);
            Bukkit.getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }

            target.sendPacket(new PacketPlaySound(music, event.getVolumePercent()));
        }
    }

    public void stopMusic(SpoutPlayer target) {
        stopMusic(target, true);
    }

    public void stopMusic(SpoutPlayer target, boolean resetTimer) {
        stopMusic(target, true, -1);
    }

    public void stopMusic(SpoutPlayer target, boolean resetTimer, int fadeOutTime) {
        if (target.isSpoutCraftEnabled()) {
            target.sendPacket(new PacketStopMusic(resetTimer, fadeOutTime));
        }
    }

    // custom sound effects
    
    public void playGlobalCustomSoundEffect(Plugin plugin, String url, boolean notify) {
        playGlobalCustomSoundEffect(plugin, url, notify, null);
    }

    public void playGlobalCustomSoundEffect(Plugin plugin, String url, boolean notify, Location location) {
        playGlobalCustomSoundEffect(plugin, url, notify, location, 16);
    }

    public void playGlobalCustomSoundEffect(Plugin plugin, String url, boolean notify, Location location, int distance) {
        playGlobalCustomSoundEffect(plugin, url, notify, location, distance, 100);
    }

    public void playGlobalCustomSoundEffect(Plugin plugin, String url, boolean notify, Location location, int distance, int volumePercent) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), url, notify, location, distance, volumePercent);
        }
    }

    public void playCustomSoundEffect(Plugin plugin, SpoutPlayer target, String url, boolean notify) {
        playCustomSoundEffect(plugin, target, url, notify, null);
    }

    public void playCustomSoundEffect(Plugin plugin, SpoutPlayer target, String url, boolean notify, Location location) {
        playCustomSoundEffect(plugin, target, url, notify, location, 16);
    }

    public void playCustomSoundEffect(Plugin plugin, SpoutPlayer target, String url, boolean notify, Location location, int distance) {
        playCustomSoundEffect(plugin, target, url, notify, location, distance, 100);
    }

    public void playCustomSoundEffect(Plugin plugin, SpoutPlayer target, String url, boolean notify, Location location, int distance, int volumePercent) {
        playCustomFile(plugin, target, url, notify, location, distance, volumePercent, true);
    }

    // custom music
    
    public void playGlobalCustomMusic(Plugin plugin, String url, boolean notify) {
        playGlobalCustomMusic(plugin, url, notify, null);
    }

    public void playGlobalCustomMusic(Plugin plugin, String url, boolean notify, Location location) {
        playGlobalCustomMusic(plugin, url, notify, location, 16);
    }

    public void playGlobalCustomMusic(Plugin plugin, String url, boolean notify, Location location, int distance) {
        playGlobalCustomMusic(plugin, url, notify, location, distance, 100);
    }

    public void playGlobalCustomMusic(Plugin plugin, String url, boolean notify, Location location, int distance, int volumePercent) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playCustomMusic(plugin, SpoutManager.getPlayer(player), url, notify, location, distance, volumePercent);
        }
    }

    public void playCustomMusic(Plugin plugin, SpoutPlayer target, String url, boolean notify) {
        playCustomMusic(plugin, target, url, notify, null);
    }

    public void playCustomMusic(Plugin plugin, SpoutPlayer target, String url, boolean notify, Location location) {
        playCustomMusic(plugin, target, url, notify, location, 16);
    }

    public void playCustomMusic(Plugin plugin, SpoutPlayer target, String url, boolean notify, Location location, int distance) {
        playCustomMusic(plugin, target, url, notify, location, distance, 100);
    }

    public void playCustomMusic(Plugin plugin, SpoutPlayer target, String url, boolean notify, Location location, int distance, int volumePercent) {
        playCustomFile(plugin, target, url, notify, location, distance, volumePercent, false);
    }

    // helper
    
    private void playCustomFile(Plugin plugin, SpoutPlayer target, String url, boolean notify, Location location, int distance, int volumePercent, boolean soundEffect) {
        if (target.isSpoutCraftEnabled()) {
            if (url.length() > 255 || url.length() < 5) {
                throw new IllegalArgumentException("All urls must be between 5 and 255 characters");
            }

            String extension = url.substring(url.length() - 4, url.length());
            if (extension.equalsIgnoreCase(".ogg") || extension.equalsIgnoreCase(".wav") || extension.matches(".*[mM][iI][dD][iI]?$")) {
                if (location == null || location.getWorld().equals(target.getWorld())) {
                    if (!soundEffect) {
                        BackgroundMusicEvent event = new BackgroundMusicEvent(url, volumePercent, target);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                        if (event.isCancelled()) {
                            return;
                        }
                        volumePercent = event.getVolumePercent();
                    }
                    target.sendPacket(new PacketDownloadMusic(plugin != null ? plugin.getDescription().getName() : "temp", url, location, distance, volumePercent, soundEffect, notify));
                }
            } else {
                throw new IllegalArgumentException("All audio files must be ogg vorbis, wav, or midi type");
            }
        }
    }
    
}
