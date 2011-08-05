package net.glowstone.spout;

import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.getspout.spoutapi.player.PlayerManager;
import org.getspout.spoutapi.player.SpoutPlayer;

import net.glowstone.entity.GlowPlayer;
import net.glowstone.GlowServer;

/**
 * Player manager for Spout integration.
 */
public class GlowPlayerManager implements PlayerManager {
    
    private final HashSet<Player> nagList = new HashSet<Player>();

	public SpoutPlayer getPlayer(Player player) {
        if (player == null) {
            throw new NullPointerException();
        }
        
		if (player instanceof GlowPlayer) {
            // all good
            return (SpoutPlayer) player;
        } else if (player instanceof SpoutPlayer) {
            // should never happen, but...
            if (!nagList.contains(player)) {
                GlowServer.logger.log(Level.SEVERE, "SpoutPlayer {0} was not a GlowPlayer, instead {1}", new Object[]{player.getName(), player.getClass().getName()});
                nagList.add(player);
            }
            return (SpoutPlayer) player;
        } else {
            // should never happen!
            if (!nagList.contains(player)) {
                GlowServer.logger.log(Level.SEVERE, "Player {0} was not a GlowPlayer, instead {1}", new Object[]{player.getName(), player.getClass().getName()});
                nagList.add(player);
            }
            throw new IllegalStateException("Player was not a GlowPlayer");
        }
	}

	public SpoutPlayer getPlayer(UUID id) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getUniqueId().equals(id)) {
				return getPlayer(player);
			}
		}
		return null;
	}

	public SpoutPlayer getPlayer(int entityId) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getEntityId() == entityId) {
				return getPlayer(player);
			}
		}
		return null;
	}

}
