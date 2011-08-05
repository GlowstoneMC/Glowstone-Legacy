package net.glowstone.spout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.plugin.Plugin;

import org.getspout.spoutapi.keyboard.Keyboard;
import org.getspout.spoutapi.keyboard.KeyboardBinding;
import org.getspout.spoutapi.keyboard.KeyboardManager;
import org.getspout.spoutapi.player.SpoutPlayer;

/**
 * Keyboard manager for Spout integration. Cleaned up SimpleKeyboardManager from
 * the Spout plugin for CraftBukkit, used with permission.
 */
public class GlowKeyboardManager implements KeyboardManager {

    protected HashSet<Plugin> pluginSet;
    protected HashMap<Plugin, HashMap<Keyboard, HashSet<KeyboardBinding>>> keyMap;

    public GlowKeyboardManager() {
        pluginSet = new HashSet<Plugin>();
        keyMap = new HashMap<Plugin, HashMap<Keyboard, HashSet<KeyboardBinding>>>();
    }

    public int getNumKeyBindings(Keyboard key) {
        int size = 0;
        for (Plugin plugin : pluginSet) {
            size += keyMap.get(plugin).get(key).size();
        }
        return size;
    }

    public void addKeyBinding(Keyboard key, KeyboardBinding keyBinding, Plugin plugin) {
        if (!pluginSet.contains(plugin)) {
            pluginSet.add(plugin);
            keyMap.put(plugin, new HashMap<Keyboard, HashSet<KeyboardBinding>>());
        }
        keyMap.get(plugin).get(key).add(keyBinding);
    }

    public void removeKeyBinding(Keyboard key, Class<? extends KeyboardBinding> keyBindingClass, Plugin plugin) {
        HashSet<KeyboardBinding> set = keyMap.get(plugin).get(key);
        Iterator<KeyboardBinding> i = set.iterator();
        while (i.hasNext()) {
            KeyboardBinding binding = i.next();
            if (keyBindingClass.isInstance(binding)) {
                i.remove();
            }
        }
    }

    public void removeAllKeyBindings(Plugin plugin) {
        keyMap.put(plugin, new HashMap<Keyboard, HashSet<KeyboardBinding>>());
    }

    public void onPreKeyPress(Keyboard key, SpoutPlayer player) {
        HashSet<KeyboardBinding> set;
        for (Plugin plugin : pluginSet) {
            set = keyMap.get(plugin).get(key);
            for (KeyboardBinding binding : set) {
                binding.onPreKeyPress(player);
            }
        }
    }

    public void onPostKeyPress(Keyboard key, SpoutPlayer player) {
        HashSet<KeyboardBinding> set;
        for (Plugin plugin : pluginSet) {
            set = keyMap.get(plugin).get(key);
            for (KeyboardBinding binding : set) {
                binding.onPostKeyPress(player);
            }
        }
    }

    public void onPreKeyRelease(Keyboard key, SpoutPlayer player) {
        HashSet<KeyboardBinding> set;
        for (Plugin plugin : pluginSet) {
            set = keyMap.get(plugin).get(key);
            for (KeyboardBinding binding : set) {
                binding.onPreKeyRelease(player);
            }
        }
    }

    public void onPostKeyRelease(Keyboard key, SpoutPlayer player) {
        HashSet<KeyboardBinding> set;
        for (Plugin plugin : pluginSet) {
            set = keyMap.get(plugin).get(key);
            for (KeyboardBinding binding : set) {
                binding.onPostKeyRelease(player);
            }
        }
    }
    
}
