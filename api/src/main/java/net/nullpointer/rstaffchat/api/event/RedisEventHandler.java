package net.nullpointer.rstaffchat.api.event;

/**
 * Redis event handler
 * @param <T> redis event type
 */
@FunctionalInterface
public interface RedisEventHandler<T extends RedisEvent> {
    /**
     * Handle redis event
     * @param event event instance
     */
    void handle(T event);
}
