package net.nullpointer.rstaffchat.plugin.util;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface ExternalPlaceholderProvider {
    ExternalPlaceholderProvider NONE = (player, component) -> component;

    Component setPlaceholders(Player player, Component component);
}
