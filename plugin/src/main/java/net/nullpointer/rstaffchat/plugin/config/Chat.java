package net.nullpointer.rstaffchat.plugin.config;

import java.util.Set;

public record Chat(String id, Set<String> aliases, String permission, String format) {
    public static final Chat DEFAULT = new Chat(
            "staff",
            Set.of("sc"),
            "rstaff.chat",
            "<red>[<source>] [STAFF] <gray><sender> -> <yellow><message>"
    );

    public static Chat fromId(String id) {
        return new Chat(
                id,
                Set.of(id),
                "rstaff.chat." + id,
                "<red>[<source>] [%s] <gray><sender> -> <yellow><message>".formatted(id.toUpperCase())
        );
    }
}
