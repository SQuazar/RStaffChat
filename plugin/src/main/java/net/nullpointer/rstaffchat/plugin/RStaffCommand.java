package net.nullpointer.rstaffchat.plugin;

import lombok.RequiredArgsConstructor;
import net.nullpointer.rstaffchat.plugin.service.StaffChatService;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class RStaffCommand implements CommandExecutor {
    private final StaffChatService staffChatService;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§cСообщение не может быть пустым!");
            return true;
        }

        if (sender instanceof Player player) {
            staffChatService.broadcastStaff(player, String.join(" ", args));
            return true;
        }
        if (sender instanceof ConsoleCommandSender console) {
            staffChatService.broadcastStaff(console, String.join(" ", args));
            return true;
        }
        if (sender instanceof RemoteConsoleCommandSender remote) {
            staffChatService.broadcastStaff(remote, String.join(" ", args));
            return true;
        }
        sender.sendMessage("§cВам не разрешено отправлять сообщения в стафф-чат!");
        return true;
    }
}
