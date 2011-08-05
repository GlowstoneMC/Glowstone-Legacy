package net.glowstone.spout;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.packet.PacketSky;
import org.getspout.spoutapi.player.SkyManager;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * Sky manager for Spout integration. Modified SimpleSkyManager from the
 * Spout plugin for CraftBukkit, used with permission.
 */
public class GlowSkyManager implements SkyManager {

    private final HashMap<String, Integer> cloudHeight = new HashMap<String, Integer>();
    private final HashMap<String, Integer> starFreq = new HashMap<String, Integer>();
    private final HashMap<String, Integer> sunSize = new HashMap<String, Integer>();
    private final HashMap<String, Integer> moonSize = new HashMap<String, Integer>();
    private final HashMap<String, String> sunUrl = new HashMap<String, String>();
    private final HashMap<String, String> moonUrl = new HashMap<String, String>();

    // clouds
    
    public int getCloudHeight(SpoutPlayer player) {
        return cloudHeight.containsKey(player.getName()) ? cloudHeight.get(player.getName()) : 108;
    }

    public void setCloudHeight(SpoutPlayer player, int y) {
        cloudHeight.put(player.getName(), y);
        player.sendPacket(new PacketSky(y, 0, 0, 0));
    }

    public boolean isCloudsVisible(SpoutPlayer player) {
        return cloudHeight.containsKey(player.getName()) ? cloudHeight.get(player.getName()) >= 0 : true;
    }

    public void setCloudsVisible(SpoutPlayer player, boolean visible) {
        if (isCloudsVisible(player) != visible) {
            setCloudHeight(player, visible ? 108 : -1);
        }
    }
    
    // stars

    public int getStarFrequency(SpoutPlayer player) {
        return starFreq.containsKey(player.getName()) ? starFreq.get(player.getName()) : 1500;
    }

    public void setStarFrequency(SpoutPlayer player, int frequency) {
        starFreq.put(player.getName(), frequency);
        player.sendPacket(new PacketSky(0, frequency, 0, 0));
    }

    public boolean isStarsVisible(SpoutPlayer player) {
        return starFreq.containsKey(player.getName()) ? starFreq.get(player.getName()) > -1 : true;
    }

    public void setStarsVisible(SpoutPlayer player, boolean visible) {
        if (isStarsVisible(player) != visible) {
            setStarFrequency(player, visible ? 1500 : -1);
        }
    }

    // sun
    
    public int getSunSizePercent(SpoutPlayer player) {
        return sunSize.containsKey(player.getName()) ? moonSize.get(player.getName()) : 100;
    }

    public void setSunSizePercent(SpoutPlayer player, int percent) {
        sunSize.put(player.getName(), percent);
        player.sendPacket(new PacketSky(0, 0, percent, 0));
    }

    public boolean isSunVisible(SpoutPlayer player) {
        return sunSize.containsKey(player.getName()) ? sunSize.get(player.getName()) >= 0 : true;
    }

    public void setSunVisible(SpoutPlayer player, boolean visible) {
        if (isSunVisible(player) != visible) {
            setSunSizePercent(player, visible ? 100 : -1);
        }
    }

    public String getSunTextureUrl(SpoutPlayer player) {
        return sunUrl.containsKey(player.getName()) ? sunUrl.get(player.getName()) : null;
    }

    public void setSunTextureUrl(SpoutPlayer player, String url) {
        if (url == null) {
            sunUrl.remove(player.getName());
            player.sendPacket(new PacketSky("[reset]", ""));
        } else {
            checkUrl(url);
            sunUrl.put(player.getName(), url);
            player.sendPacket(new PacketSky(url, ""));
        }
    }
    
    // moon
    
    public int getMoonSizePercent(SpoutPlayer player) {
        return moonSize.containsKey(player.getName()) ? moonSize.get(player.getName()) : 100;
    }

    public void setMoonSizePercent(SpoutPlayer player, int percent) {
        moonSize.put(player.getName(), percent);
        player.sendPacket(new PacketSky(0, 0, percent, 0));
    }

    public boolean isMoonVisible(SpoutPlayer player) {
        return moonSize.containsKey(player.getName()) ? moonSize.get(player.getName()) >= 0 : true;
    }

    public void setMoonVisible(SpoutPlayer player, boolean visible) {
        if (isMoonVisible(player) != visible) {
            setMoonSizePercent(player, visible ? 100 : -1);
        }
    }

    public String getMoonTextureUrl(SpoutPlayer player) {
        return moonUrl.containsKey(player.getName()) ? moonUrl.get(player.getName()) : null;
    }

    public void setMoonTextureUrl(SpoutPlayer player, String url) {
        if (url == null) {
            moonUrl.remove(player.getName());
            player.sendPacket(new PacketSky("[reset]", ""));
        } else {
            checkUrl(url);
            moonUrl.put(player.getName(), url);
            player.sendPacket(new PacketSky(url, ""));
        }
    }

    // misc
    
    public void registerPlayer(SpoutPlayer player) {
        if (player.isSpoutCraftEnabled()) {
            String moon = getMoonTextureUrl(player);
            moon = moon == null ? "" : moon;
            String sun = getSunTextureUrl(player);
            sun = sun == null ? "" : sun;
            player.sendPacket(new PacketSky(getRealCloudHeight(player), getStarFrequency(player), getSunSizePercent(player), getMoonSizePercent(player), sun, moon));
        }
    }

    public void resetAll() {
        cloudHeight.clear();
        starFreq.clear();
        sunSize.clear();
        moonSize.clear();
        sunUrl.clear();
        moonUrl.clear();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            SpoutManager.getPlayer(player).sendPacket(new PacketSky(108, 1500, 100, 100, "[reset]", "[reset]"));
        }
    }
    
    // helpers

    private int getRealCloudHeight(SpoutPlayer player) {
        return cloudHeight.containsKey(player.getName()) ? cloudHeight.get(player.getName()) : -999; // magic number
    }

    private void checkUrl(String url) {
        if (url == null || url.length() < 5) {
            throw new IllegalArgumentException("Invalid URL specified");
        }
        if (!url.substring(url.length() - 4, url.length()).equalsIgnoreCase(".png")) {
            throw new IllegalArgumentException("All skins must be a PNG image");
        }
        if (url.length() > 255) {
            throw new IllegalArgumentException("All urls must be shorter than 256 characters");
        }
    }
    
}
