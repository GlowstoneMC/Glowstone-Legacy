package net.glowstone.command;

import net.glowstone.GlowServer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author zml2008
 */
public class BanCommand extends GlowCommand {

    public BanCommand(GlowServer server) {
        super(server, "ban", "Manage player and ip bans", "[-ip] add|remove|check name|ip");
    }
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        int mod = 0;
        boolean ip = false;
        if (!checkArgs(sender, args, 2, 3) || !checkOp(sender)) return false;
        if (args[0].equalsIgnoreCase("-ip")) {
            ip = true; mod = 1;
        }
        String option = args[mod];
        String target = args[1 + mod];
        Player player = server.getPlayer(target);
        String senderName = (sender instanceof Player? ((Player) sender).getDisplayName(): "Console");
        if (option.equalsIgnoreCase("add")) {
            if (ip) {
                String newTarget = target;
                if (player != null) newTarget = player.getAddress().getAddress().getHostAddress();
                server.getBanManager().setIpBanned(newTarget, true);
            } else {
                server.getBanManager().setBanned(target, true);
            }
            if (player != null) player.kickPlayer("You have been " + (ip ? "ip banned" : "banned") + " by " + senderName);
            server.broadcastMessage(ChatColor.RED + target + " has been banned by " + senderName);
        } else if (option.equalsIgnoreCase("remove")) {
           if (ip) {
                server.getBanManager().setIpBanned(target, false);
            } else {
                server.getBanManager().setBanned(target, false);
            }
            sender.sendMessage(ChatColor.RED + target + " was unbanned.");
        } else if (option.equalsIgnoreCase("check")) {
            boolean banned;
            if (ip) {
                banned = server.getBanManager().isIpBanned(target);
            } else {
                banned = server.getBanManager().isBanned(target);
            }
            sender.sendMessage(ChatColor.RED + target + (banned ? " is" : " is not") + " banned.");
        } else {
            sender.sendMessage(ChatColor.GRAY + "Invalid option. Usage: " + usageMessage);
            return false;
        }
        return true;
    }
}
