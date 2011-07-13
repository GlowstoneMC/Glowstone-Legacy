package net.glowstone.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.glowstone.GlowServer;

/**
 * A built-in command to kick a person off the server.
 * @author Zhuowei
 */
public class KickCommand extends GlowCommand {
    
    public KickCommand(GlowServer server) {
        super(server, "kick", "Kick a player off the server", "<player>");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!checkArgs(sender, args, 1)) {
            return false;
        } else if (!checkOp(sender)) {
            return false;
        } else {
            Player player = server.getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage("No such player");
                return false;
            }
            player.kickPlayer("Kicked by " +
                (sender instanceof Player? ((Player) sender).getDisplayName(): "Console"));
            server.broadcastMessage(args[0] + " has been kicked");
            return true;
        }
    }
    
}
