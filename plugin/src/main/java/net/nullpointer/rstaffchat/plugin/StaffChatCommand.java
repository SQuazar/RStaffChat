package net.nullpointer.rstaffchat.plugin;

import net.nullpointer.rstaffchat.plugin.config.Chat;
import net.nullpointer.rstaffchat.plugin.service.StaffChatService;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class StaffChatCommand extends Command {
    private final Chat chat;
    private final StaffChatService staffChatService;

    StaffChatCommand(@NotNull Chat chat, StaffChatService staffChatService) {
        super(chat.id(), "Отправить сообщение в стафф-чат", "/" + chat.id(), new ArrayList<>(chat.aliases()));
        setPermission(chat.permission());
        this.chat = chat;
        this.staffChatService = staffChatService;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cСообщение не может быть пустым!");
            return true;
        }

        if (sender instanceof Player player) {
            staffChatService.broadcastStaff(player, chat, String.join(" ", args));
            return true;
        }
        if (sender instanceof ConsoleCommandSender console) {
            staffChatService.broadcastStaff(console, chat, String.join(" ", args));
            return true;
        }
        if (sender instanceof RemoteConsoleCommandSender remote) {
            staffChatService.broadcastStaff(remote, chat, String.join(" ", args));
            return true;
        }
        sender.sendMessage("§cВам не разрешено отправлять сообщения в стафф-чат!");
        return true;
    }
}
