package net.glowstone.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.glowstone.GlowServer;
import net.glowstone.util.StringUtils;
import org.bukkit.ChatColor;

/**
 * A built-in command to display the message in form of * USER message
 * @author Zhuowei
 */
public class MeCommand extends GlowCommand {
    
    public MeCommand(GlowServer server) {
        super(server, "me", "Displays message in form of * USER message", "<message>");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (args.length < 1) {
            return false;
        }
        String senderName = (sender instanceof Player? ((Player) sender).getDisplayName(): "Console");
        server.broadcastMessage(ChatColor.WHITE + " * " + senderName + " " + StringUtils.join(args, " "));
        return true;
    }
    
}
