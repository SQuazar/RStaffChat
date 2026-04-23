package net.nullpointer.rstaffchat.plugin.util;

public final class LegacyColor {
    public static String normalizeMm(String s) {
        if (!containsLegacyColors(s)) return s;
        return s.replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&k", "<obfuscated>")
                .replace("&l", "<bold>")
                .replace("&m", "<strikethrough>")
                .replace("&n", "<underlined>")
                .replace("&o", "<italic>")
                .replace("&r", "<reset>");
    }

    private static boolean containsLegacyColors(String input) {
        for (int i = 0; i < input.length() - 1; i++)
            if (input.charAt(i) == '&' && isLegacyCode(input.charAt(i + 1)))
                return true;
        return false;
    }

    private static boolean isLegacyCode(char c) {
        c = Character.toLowerCase(c);
        return (c >= '0' && c <= '9')
               || (c >= 'a' && c <= 'f')
               || c == 'k'
               || c == 'l'
               || c == 'm'
               || c == 'n'
               || c == 'o'
               || c == 'r';
    }
}
