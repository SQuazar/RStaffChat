package net.nullpointer.rstaffchat.plugin.service;

import net.nullpointer.rstaffchat.plugin.config.Chat;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public interface StaffChatService {
    void broadcastStaff(Player player, Chat chat, String message);

    void broadcastStaff(ConsoleCommandSender sender, Chat chat, String message);

    void broadcastStaff(RemoteConsoleCommandSender sender, Chat chat, String message);
}
