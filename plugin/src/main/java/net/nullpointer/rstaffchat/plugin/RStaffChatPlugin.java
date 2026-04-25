package net.nullpointer.rstaffchat.plugin;

import net.nullpointer.rstaffchat.api.RStaffChatAPI;
import net.nullpointer.rstaffchat.core.RStaffChatAPIImpl;
import net.nullpointer.rstaffchat.plugin.config.Chat;
import net.nullpointer.rstaffchat.plugin.config.RStaffConfig;
import net.nullpointer.rstaffchat.plugin.service.StaffChatService;
import net.nullpointer.rstaffchat.plugin.service.StaffChatServiceImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class RStaffChatPlugin extends JavaPlugin {
    private RStaffConfig config;
    private RStaffChatAPI api;
    private StaffChatService staffChatService;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = new RStaffConfig(this);
        config.load();

        setupService();
        registerCommands();
    }

    private void setupService() {

        this.api = RStaffChatAPIImpl.builder()
                .channel("rstaff:events")
                .dispatcher(task -> Bukkit.getScheduler().runTask(this, task))
                .redisUri(config.getRedisUri())
                .build();

        this.api.init();

        this.staffChatService = new StaffChatServiceImpl(api, config);

        Bukkit.getServicesManager().register(
                RStaffChatAPI.class,
                this.api,
                this,
                ServicePriority.Lowest
        );
        Bukkit.getServicesManager().register(
                StaffChatService.class,
                this.staffChatService,
                this,
                ServicePriority.Lowest
        );
    }

    private void registerCommands() {
        for (Chat chat : config.getChats()) {
            Bukkit.getCommandMap().register("rstaff", new StaffChatCommand(chat, staffChatService));
        }
    }

    private void unregisterCommands() {
        CommandMap map = Bukkit.getCommandMap();
        List<String> remove = map.getKnownCommands().entrySet().stream()
                .filter(e -> e.getValue() instanceof StaffChatCommand)
                .map(Map.Entry::getKey)
                .toList();
        remove.forEach(key -> {
            map.getCommand(key).unregister(map);
            map.getKnownCommands().remove(key);
        });
    }

    @Override
    public void onDisable() {
        if (api != null) api.shutdown();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        reloadConfig();
        unregisterCommands();
        config.load();
        registerCommands();
        sender.sendMessage("§aКонфиг перезагружен!");
        return true;
    }
}
