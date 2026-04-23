package net.nullpointer.rstaffchat.plugin.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public final class PlaceholderAPIProvider implements ExternalPlaceholderProvider{
    @Override
    public String setPlaceholders(Player player, String s) {
        return PlaceholderAPI.setPlaceholders(player, s);
    }
}
