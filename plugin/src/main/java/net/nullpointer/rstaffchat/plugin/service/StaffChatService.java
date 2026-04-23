package net.nullpointer.rstaffchat.plugin.service;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public interface StaffChatService {
    void broadcastStaff(Player player, String message);

    void broadcastStaff(ConsoleCommandSender sender, String message);

    void broadcastStaff(RemoteConsoleCommandSender sender, String message);
}
