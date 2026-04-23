package net.nullpointer.rstaffchat.api.redis;

import net.nullpointer.rstaffchat.api.event.RedisEvent;
import net.nullpointer.rstaffchat.api.event.RedisEventHandler;

/**
 * Redis event subscriber
 */
public interface RedisSubscriber {
    /**
     * Subscribe to redis event
     * @param type event type name (staff.chat for example)
     * @param clazz type class of event
     * @param handler event handler
     * @param <T> redis event type
     */
    <T extends RedisEvent> void subscribe(String type, Class<T> clazz, RedisEventHandler<T> handler);
}
