package net.glowstone.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.util.Collections;

/**
 * A built-in command to demonstrate all chat colors.
 */
public class ColorCommand extends BukkitCommand {
    
    public ColorCommand() {
        super("colours", "Display all colours.", "/colour", Collections.<String>emptyList());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        ChatColor[] values = ChatColor.values();
        for (int i = 0; i < values.length; i += 2) {
            sender.sendMessage(values[i] + values[i].name() + ChatColor.WHITE + " -- " + values[i + 1] + values[i + 1].name());
        }
        return true;
    }

}
