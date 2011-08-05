package net.glowstone.spout;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.packet.PacketEntityTitle;
import org.getspout.spoutapi.packet.PacketSkinURL;
import org.getspout.spoutapi.player.AppearanceManager;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * Appearance manager for Spout integration.
 */
public class GlowAppearanceManager implements AppearanceManager {

    HashMap<String, String> globalSkinMap = new HashMap<String, String>();
    HashMap<String, HashMap<String, String>> skinMap = new HashMap<String, HashMap<String, String>>();
    HashMap<String, String> globalCloakMap = new HashMap<String, String>();
    HashMap<String, HashMap<String, String>> cloakMap = new HashMap<String, HashMap<String, String>>();
    HashMap<Integer, String> globalTitleMap = new HashMap<Integer, String>();
    HashMap<String, HashMap<Integer, String>> titleMap = new HashMap<String, HashMap<Integer, String>>();

    // skins

    public String getSkinUrl(SpoutPlayer viewingPlayer, HumanEntity target) {
        HashMap<String, String> skMap = getSkinMap(viewingPlayer.getName());
        return skMap.containsKey(target.getName()) ? skMap.get(target.getName()) :
                globalSkinMap.containsKey(target.getName()) ? globalSkinMap.get(target.getName()) :
                getDefaultSkin(target);
    }

    public void setPlayerSkin(SpoutPlayer viewingPlayer, HumanEntity target, String url) {
        checkUrl(url);
        getSkinMap(viewingPlayer.getName()).put(target.getName(), url);
        viewingPlayer.sendPacket(new PacketSkinURL(target.getEntityId(), url));
    }
    
    public void setGlobalSkin(HumanEntity target, String url) {
        checkUrl(url);
        globalSkinMap.put(target.getName(), url);
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            setPlayerSkin(SpoutManager.getPlayer(player), target, url);
        }
    }

    public void resetGlobalSkin(HumanEntity target) {
        setGlobalSkin(target, getDefaultSkin(target));
    }

    public void resetPlayerSkin(SpoutPlayer viewingPlayer, HumanEntity target) {
        setPlayerSkin(viewingPlayer, target, getDefaultSkin(target));
    }

    public void resetAllSkins() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            skinMap.put(player.getName(), new HashMap<String, String>());
        }
        for (World world : Bukkit.getServer().getWorlds()) {
            for (LivingEntity living : world.getLivingEntities()) {
                if (living instanceof HumanEntity) {
                    resetGlobalSkin((HumanEntity) living);
                }
            }
        }
    }
    
    // cloaks

    public String getCloakUrl(SpoutPlayer viewingPlayer, HumanEntity target) {
        HashMap<String, String> clMap = getCloakMap(viewingPlayer.getName());
        return clMap.containsKey(target.getName()) ? clMap.get(target.getName()) :
                globalCloakMap.containsKey(target.getName()) ? globalCloakMap.get(target.getName()) :
                getDefaultCloak(target);
    }
    
    public void setPlayerCloak(SpoutPlayer viewingPlayer, HumanEntity target, String url) {
        checkUrl(url);
        getCloakMap(viewingPlayer.getName()).put(target.getName(), url);
        viewingPlayer.sendPacket(new PacketSkinURL(url, target.getEntityId()));
    }
    
    public void setGlobalCloak(HumanEntity target, String url) {
        checkUrl(url);
        globalCloakMap.put(target.getName(), url);
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            setPlayerCloak(SpoutManager.getPlayer(player), target, url);
        }
    }

    public void resetGlobalCloak(HumanEntity target) {
        setGlobalCloak(target, getDefaultCloak(target));
    }

    public void resetPlayerCloak(SpoutPlayer viewingPlayer, HumanEntity target) {
        setPlayerCloak(viewingPlayer, target, getDefaultCloak(target));
    }

    public void resetAllCloaks() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            cloakMap.put(player.getName(), new HashMap<String, String>());
        }
        for (World world : Bukkit.getServer().getWorlds()) {
            for (LivingEntity living : world.getLivingEntities()) {
                if (living instanceof HumanEntity) {
                    resetGlobalCloak((HumanEntity) living);
                }
            }
        }
    }

    // titles
    
    public String getTitle(SpoutPlayer viewingPlayer, LivingEntity target) {
        HashMap<Integer, String> tMap = getTitleMap(viewingPlayer.getName());
        return tMap.containsKey(target.getEntityId()) ? tMap.get(target.getEntityId()) :
                globalTitleMap.containsKey(target.getEntityId()) ? globalTitleMap.get(target.getEntityId()) :
                viewingPlayer.getName(); // wait, what?
    }
    
    public void setPlayerTitle(SpoutPlayer viewingPlayer, LivingEntity target, String title) {
        getTitleMap(viewingPlayer.getName()).put(target.getEntityId(), title);
        viewingPlayer.sendPacket(new PacketEntityTitle(target.getEntityId(), title));
    }

    public void setGlobalTitle(LivingEntity target, String title) {
        globalTitleMap.put(target.getEntityId(), title);
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            setPlayerTitle(SpoutManager.getPlayer(player), target, title);
        }
    }

    public void hidePlayerTitle(SpoutPlayer viewingPlayer, LivingEntity target) {
        setPlayerTitle(viewingPlayer, target, "[hide]");
    }

    public void hideGlobalTitle(LivingEntity target) {
        setGlobalTitle(target, "[hide]");
    }

    public void resetPlayerTitle(SpoutPlayer viewingPlayer, LivingEntity target) {
        getTitleMap(viewingPlayer.getName()).remove(target.getEntityId());
        viewingPlayer.sendPacket(new PacketEntityTitle(target.getEntityId(), "reset"));
    }

    public void resetGlobalTitle(LivingEntity target) {
        globalTitleMap.remove(target.getEntityId());
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            resetPlayerTitle(SpoutManager.getPlayer(player), target);
        }
    }

    public void resetAllTitles() {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (LivingEntity living : world.getLivingEntities()) {
                resetGlobalTitle(living);
            }
        }
    }

    // misc

    public void resetAll() {
        resetAllCloaks();
        resetAllSkins();
        resetAllTitles();
    }
    
    public void registerPlayer(SpoutPlayer player) {
        if (!player.isSpoutCraftEnabled()) {
            return;
        }

        for (World world : Bukkit.getServer().getWorlds()) {
            for (LivingEntity living : world.getLivingEntities()) {
                // titles
                String title = getTitle(player, living);
                if (title != null) {
                    player.sendPacket(new PacketEntityTitle(living.getEntityId(), title));
                }
                
                // skins/cloaks
                if (living instanceof HumanEntity) {
                    HumanEntity human = (HumanEntity) living;
                    player.sendPacket(new PacketSkinURL(living.getEntityId(), getSkinUrl(player, human), getCloakUrl(player, human)));
                }
            }
        }
    }

    // helpers

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

    private HashMap<Integer, String> getTitleMap(String player) {
        if (!titleMap.containsKey(player)) {
            titleMap.put(player, new HashMap<Integer, String>());
        }
        return titleMap.get(player);
    }

    private HashMap<String, String> getSkinMap(String player) {
        if (!skinMap.containsKey(player)) {
            skinMap.put(player, new HashMap<String, String>());
        }
        return skinMap.get(player);
    }

    private HashMap<String, String> getCloakMap(String player) {
        if (!cloakMap.containsKey(player)) {
            cloakMap.put(player, new HashMap<String, String>());
        }
        return cloakMap.get(player);
    }

    private String getDefaultSkin(HumanEntity human) {
        return "http://s3.amazonaws.com/MinecraftSkins/" + human.getName() + ".png";
    }

    private String getDefaultCloak(HumanEntity human) {
        return "http://s3.amazonaws.com/MinecraftCloaks/" + human.getName() + ".png";
    }
    
}
