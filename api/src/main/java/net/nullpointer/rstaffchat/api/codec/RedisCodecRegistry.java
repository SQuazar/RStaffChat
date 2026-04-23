package net.nullpointer.rstaffchat.api.codec;

import net.nullpointer.rstaffchat.api.event.RedisEvent;

/**
 * Codec registry
 */
public interface RedisCodecRegistry {
    /**
     * Register codec in registry
     * @param codec event codec instance
     * @param <T> redis event type
     */
    <T extends RedisEvent> void register(RedisEventCodec<T> codec);

    /**
     * Gets redis event codec from registry
     * @param type type name (staff.chat for example)
     * @return event codec instance
     * @param <T> redis event type
     */
    <T extends RedisEvent> RedisEventCodec<T> get(String type);
}
