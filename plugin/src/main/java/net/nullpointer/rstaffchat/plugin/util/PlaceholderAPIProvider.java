package net.nullpointer.rstaffchat.plugin.util;

import me.clip.placeholderapi.PAPIComponents;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public final class PlaceholderAPIProvider implements ExternalPlaceholderProvider{
    @Override
    public Component setPlaceholders(Player player, Component component) {
        return PAPIComponents.setPlaceholders(player, component);
    }
}
