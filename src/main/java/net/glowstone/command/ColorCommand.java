package net.glowstone.command;

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

import net.glowstone.GlowServer;

/**
 * A built-in command to demonstrate all chat colors.
 * @author Tad
 */
public class ColorCommand extends GlowCommand {
    
    public ColorCommand(GlowServer server) {
        super(server, "color", "Display all colors.", "");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        String[] names = new String[] {
            "BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA",
            "DARK_RED", "DARK_PURPLE", "GOLD", "GRAY",
            "DARK_GRAY", "BLUE", "GREEN", "AQUA",
            "RED", "LIGHT_PURPLE", "YELLOW", "WHITE"
        };
        for (int i = 0; i < 16; i += 2) {
            sender.sendMessage(ChatColor.getByCode(i) + names[i] + ChatColor.WHITE + " -- " + ChatColor.getByCode(i + 1) + names[i + 1]);
        }
        return true;
    }
    
}
