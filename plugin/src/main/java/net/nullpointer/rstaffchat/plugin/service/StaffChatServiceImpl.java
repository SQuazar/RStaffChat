package net.nullpointer.rstaffchat.plugin.service;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.nullpointer.rstaffchat.api.RStaffChatAPI;
import net.nullpointer.rstaffchat.core.event.StaffChatMessageRedisEvent;
import net.nullpointer.rstaffchat.plugin.config.RStaffConfig;
import net.nullpointer.rstaffchat.plugin.util.ExternalPlaceholderProvider;
import net.nullpointer.rstaffchat.plugin.util.LegacyColor;
import net.nullpointer.rstaffchat.plugin.util.PlaceholderAPIProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.regex.Pattern;

import static net.nullpointer.rstaffchat.core.event.StaffChatMessageRedisEvent.EVENT_TYPE;

public class StaffChatServiceImpl implements StaffChatService {
    private final RStaffChatAPI api;
    private final RStaffConfig config;
    private final ExternalPlaceholderProvider placeholderProvider;

    public StaffChatServiceImpl(RStaffChatAPI api, RStaffConfig config) {
        this.api = api;
        this.config = config;
        api.subscriber().subscribe(EVENT_TYPE, StaffChatMessageRedisEvent.class, event ->
                broadcast(event.getSource(), event.getGroupPrefix(), event.getSenderName(), event.getMessage())
        );

        this.placeholderProvider = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null ?
                new PlaceholderAPIProvider() : ExternalPlaceholderProvider.NONE;
    }

    @Override
    public void broadcastStaff(Player player, String message) {
        broadcast(config.getSourceName(), null, player.getName(), message);
        api.publisher().publish(new StaffChatMessageRedisEvent(
                config.getSourceName(),
                player.getName(),
                player.getUniqueId(),
                message
        ));
    }

    @Override
    public void broadcastStaff(ConsoleCommandSender sender, String message) {
        broadcast(config.getSourceName(), null, sender.getName(), message);
        api.publisher().publish(new StaffChatMessageRedisEvent(
                config.getSourceName(),
                sender.getName(),
                RStaffChatAPI.SERVICE_UUID,
                message
        ));
    }

    @Override
    public void broadcastStaff(RemoteConsoleCommandSender sender, String message) {
        broadcast(config.getSourceName(), null, sender.getName(), message);
        api.publisher().publish(new StaffChatMessageRedisEvent(
                config.getSourceName(),
                sender.getName(),
                RStaffChatAPI.SERVICE_UUID,
                message
        ));
    }

    private void broadcast(String source, String group, String senderName, String message) {
        Component msg = format(config.getFormat(), source, group, senderName, message);
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("rstaff.chat"))
                .forEach(p -> p.sendMessage(msg));
        Bukkit.getConsoleSender().sendMessage(msg);
    }

    private Component format(String s, String source, String group, String senderName, String message) {
        Player player;
        if ((player = Bukkit.getPlayerExact(senderName)) != null)
            s = placeholderProvider.setPlaceholders(player, s);
        else s = PAPI_PATTERN.matcher(s).replaceAll("");

        s = LegacyColor.normalizeMm(s);

        return MiniMessage.miniMessage().deserialize(
                s,
                Placeholder.parsed("group", Optional.ofNullable(group).orElse("")),
                Placeholder.parsed("source", source),
                Placeholder.parsed("sender", senderName),
                Placeholder.parsed("message", message)
        );
    }

    private static final Pattern PAPI_PATTERN =
            Pattern.compile("%[^%\\s]+%");
}
