package net.glowstone.msg.handler;

import java.util.logging.Level;

import org.bukkit.ChatColor;

import net.glowstone.GlowServer;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.msg.ChatMessage;
import net.glowstone.net.Session;

public final class ChatMessageHandler extends MessageHandler<ChatMessage> {

    @Override
    public void handle(Session session, GlowPlayer player, ChatMessage message) {
        if (player == null)
            return;

        String text = message.getMessage();
        text = text.trim();
        
        if (text.length() > 100) {
            session.disconnect("Chat message too long.");
        } else {
            // Spoutcraft check
            if (!player.isSpoutCraftEnabled() && text.startsWith("/") && !text.contains(" ")) {
                String[] split = text.substring(1).split("\\.");
                if (split.length == 3) {
                    try {
                        int major = Integer.parseInt(split[0]);
                        int minor = Integer.parseInt(split[1]);
                        int build = Integer.parseInt(split[2]);
                        player.enableSpoutcraft(major, minor, build);
                        return;
                    }
                    catch(NumberFormatException ex) {}
                }
            }
            
            player.chat(text);
        }
    }

}
