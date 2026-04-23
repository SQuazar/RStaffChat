package net.nullpointer.rstaffchat.api.redis;

import net.nullpointer.rstaffchat.api.event.RedisEvent;

/**
 * Redis event publisher
 */
public interface RedisPublisher {
    /**
     * Publish redis event
     * @param event redis event instance
     * @param <T> redis event type
     */
    <T extends RedisEvent> void publish(T event);
}
