package net.nullpointer.rstaffchat.plugin.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.nullpointer.rstaffchat.plugin.RStaffChatPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public final class RStaffConfig {
    private final RStaffChatPlugin plugin;

    private String redisUri;
    private String sourceName;
    private String format;

    public void load() {
        FileConfiguration configuration = plugin.getConfig();
        redisUri = Objects.requireNonNull(configuration.getString("redis-uri"), "redis-uri cannot be null");
        sourceName = configuration.getString("server-name", Bukkit.getServer().getName());
        format = configuration.getString("format", "<red>[<source>] [STAFF] <gray><sender> -> <yellow><message>");
    }
}
