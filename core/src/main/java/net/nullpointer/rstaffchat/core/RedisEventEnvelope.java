package net.nullpointer.rstaffchat.core;

import lombok.Data;

import java.util.UUID;

@Data
class RedisEventEnvelope {
    private final UUID id;
    private final UUID source;
    private final String type;
    private final String payload;
}
