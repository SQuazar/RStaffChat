package net.nullpointer.rstaffchat.plugin.util;

import org.bukkit.entity.Player;

public interface ExternalPlaceholderProvider {
    ExternalPlaceholderProvider NONE = (player, s) -> "";

    String setPlaceholders(Player player, String s);
}
