package net.glowstone.command;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.*;
import org.bukkit.command.defaults.BanCommand;
import org.bukkit.command.defaults.HelpCommand;
import org.bukkit.command.defaults.KickCommand;
import org.bukkit.command.defaults.MeCommand;
import org.bukkit.command.defaults.TimeCommand;
import org.bukkit.command.defaults.WhitelistCommand;
import org.bukkit.command.defaults.ListCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import java.util.*;

/**
 * SimpleCommandMap that ignores certain "vanilla" commands.
 */
public class GlowCommandMap extends SimpleCommandMap {

    private final Server server;
    private static Permission parent;
    private static Permission betterParent;

    public GlowCommandMap(final Server server) {
        super(server);
        this.server = server;
    }

    @Override
    public VanillaCommand getFallback(String name) {
        return null;
    }
    
    public Set<String> getKnownCommandNames() {
        return knownCommands.keySet();
    }

    public Collection<Command> getKnownCommands() {
        return knownCommands.values();
    }

    /**
     * Perpares the glowstone command permissions. Should be only run once per plugin manager
     */
    public static void initGlowPermissions(Server server) {
        PluginManager pm = server.getPluginManager();
        parent = new Permission(GlowCommand.PERM_PREFIX, "Gives access to all Glowstone commads");
        betterParent = new Permission("glowstone", "Gives access to all Glowstone commands and functions");
        betterParent.getChildren().put(parent.getName(), true);
        pm.addPermission(betterParent);
        pm.addPermission(parent);
        parent.recalculatePermissibles();
        betterParent.recalculatePermissibles();
    }

    public void registerAll(Collection<GlowCommand> commands) {
        for (GlowCommand command : commands) {
            register(command);
        }
    }

    /**
     * Registers GlowCommands using the extra information available from them
     * @param command
     */
    public void register(GlowCommand command) {
        registerPermissions(command);
        register("#", command);
    }

    public void registerPermissions(GlowCommand command) {
        PluginManager pm = server.getPluginManager();
        Permission child = new Permission(GlowCommand.PERM_PREFIX + "." + command.getName(), command.getPermissionDefault());
        for (Permission permission : command.registerPermissions(child.getName())) {
            child.getChildren().put(permission.getName(), true);
            if (pm.getPermission(permission.getName()) == null) {
                pm.addPermission(permission);
            }
            permission.recalculatePermissibles();
        }
        parent.getChildren().put(child.getName(), true);
        if (pm.getPermission(child.getName()) == null) {
            pm.addPermission(child);
        }
        command.setPermission(child.getName());
        child.recalculatePermissibles();
        parent.recalculatePermissibles();
    }

    public void registerAllPermissions() {
        for (Command command : knownCommands.values()) {
            if (command instanceof GlowCommand) {
                registerPermissions((GlowCommand) command);
            }
        }
    }

    /**
     * Returns a list of command permissions registered through this command map.
     */
    public Set<String> getCommandPermissions() {
        HashSet<String> perms = new HashSet<String>();
        perms.add(parent.getName());
        perms.add(betterParent.getName());
        for (Command command : knownCommands.values()) {
            perms.add(command.getPermission());
            if (command instanceof GlowCommand) {
                for (Permission perm : ((GlowCommand) command).registerPermissions(parent.getName())) {
                    perms.add(perm.getName());
                }
            }
        }
        return perms;
    }
    
}
