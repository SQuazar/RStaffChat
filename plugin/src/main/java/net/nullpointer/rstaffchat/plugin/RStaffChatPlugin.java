package net.nullpointer.rstaffchat.plugin;

import net.nullpointer.rstaffchat.api.RStaffChatAPI;
import net.nullpointer.rstaffchat.core.RStaffChatAPIImpl;
import net.nullpointer.rstaffchat.plugin.config.RStaffConfig;
import net.nullpointer.rstaffchat.plugin.service.StaffChatService;
import net.nullpointer.rstaffchat.plugin.service.StaffChatServiceImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

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

        getCommand("staff").setExecutor(new RStaffCommand(staffChatService));
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

    @Override
    public void onDisable() {
        if (api != null) api.shutdown();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        reloadConfig();
        config.load();
        sender.sendMessage("§aКонфиг перезагружен!");
        return true;
    }
}
