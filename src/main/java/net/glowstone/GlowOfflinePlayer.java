package net.glowstone;

import org.bukkit.OfflinePlayer;

/**
 * @author zml2008
 */
public class GlowOfflinePlayer implements OfflinePlayer {
    private final GlowServer server;
    private final String name;

    public GlowOfflinePlayer(GlowServer server, String name) {
        this.server = server;
        this.name = name;
    }
    public boolean isOnline() {
        return false;
    }

    public String getName() {
        return name;
    }

    public boolean isBanned() {
        return server.getBanManager().isBanned(name);
    }

    public void setBanned(boolean banned) {
        server.getBanManager().setBanned(name, banned);
    }

    public boolean isOp() {
        return server.getOpsList().contains(name);
    }

    public void setOp(boolean value) {
        if (value) {
           server.getOpsList().add(name);
        } else {
            server.getOpsList().remove(name);
        }
    }
}
