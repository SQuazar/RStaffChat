package net.nullpointer.rstaffchat.plugin;

import net.nullpointer.rstaffchat.api.RStaffChatAPI;
import net.nullpointer.rstaffchat.core.RStaffChatAPIImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class RStaffChatPlugin extends JavaPlugin {
    private RStaffChatAPI api;

    @Override
    public void onEnable() {
        setupService();
    }

    private void setupService() {
        this.api = RStaffChatAPIImpl.builder()
                .channel("rstaff:events")
                .dispatcher(task -> Bukkit.getScheduler().runTask(this, task))
                .redisUri(
                        getConfig().getString("redis.uri")
                )
                .build();

        this.api.init();

        Bukkit.getServicesManager().register(
                RStaffChatAPI.class,
                this.api,
                this,
                ServicePriority.Lowest
        );
    }

    @Override
    public void onDisable() {
        if (api != null) api.shutdown();
    }
}
