package net.nullpointer.rstaffchat.api;

import net.nullpointer.rstaffchat.api.codec.RedisCodecRegistry;
import net.nullpointer.rstaffchat.api.redis.RedisPublisher;
import net.nullpointer.rstaffchat.api.redis.RedisSubscriber;

import java.util.UUID;

/**
 * RStaffChat API
 */
public interface RStaffChatAPI {
    /**
     * Service API UUID.
     * Used for not player senders
     * <p>
     * Example: Console or Remote senders
     */
    UUID SERVICE_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    /**
     * Unique ID to avoid duplicates
     */
    UUID ID = UUID.randomUUID();

    /**
     * Initialize staff chat API
     */
    void init();

    /**
     * Shutdown staff chat API
     */
    void shutdown();

    /**
     * Gets RedisCodecRegistry. Used for register event codecs
     * @return codec registry
     */
    RedisCodecRegistry codecRegistry();

    /**
     * Gets RedisPublisher. Used for publish events
     * @return redis publisher
     */
    RedisPublisher publisher();

    /**
     * Gets RedisSubscriber. Used for subscribe to events
     * @return redis subscriber
     */
    RedisSubscriber subscriber();
}
