package net.nullpointer.rstaffchat.plugin.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.nullpointer.rstaffchat.plugin.RStaffChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public final class RStaffConfig {
    private final RStaffChatPlugin plugin;

    private String redisUri;
    private String sourceName;
    private boolean allowUnknown;

    private final Map<String, Chat> chats = new HashMap<>();

    public void load() {
        chats.clear();
        FileConfiguration configuration = plugin.getConfig();
        redisUri = Objects.requireNonNull(configuration.getString("redis-uri"), "redis-uri cannot be null");
        sourceName = configuration.getString("server-name", Bukkit.getServer().getName());
        allowUnknown = configuration.getBoolean("allow-unknown-chats", false);

        ConfigurationSection section = configuration.getConfigurationSection("chat");
        if (section == null) {
            plugin.getLogger().warning("No chats found! Using default...");
            chats.put(Chat.DEFAULT.id(), Chat.DEFAULT);
            return;
        }

        for (String id : section.getKeys(false)) {
            ConfigurationSection chatSection = section.getConfigurationSection(id);
            assert chatSection != null;
            String format = chatSection.getString("format",
                    "<red>[<source>] [%s] <gray><sender> -> <yellow><message>".formatted(id.toUpperCase()));
            Set<String> aliases = chatSection.getStringList("alias").stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
            String permission = chatSection.getString("permission", "rstaff.chat." + id);
            chats.put(id, new Chat(id, aliases, permission, format));
        }
    }

    public Optional<Chat> findChat(String name) {
        Chat chat = chats.get(name);
        if (chat != null) return Optional.of(chat);
        return chats.values().stream()
                .filter(c -> c.aliases().contains(name.toLowerCase()))
                .findFirst();
    }

    public Collection<Chat> getChats() {
        return chats.values();
    }
}
