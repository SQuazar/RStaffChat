package net.nullpointer.rstaffchat.api.codec;

import net.nullpointer.rstaffchat.api.event.RedisEvent;

/**
 * Redis event codec
 * @param <T> redis event type
 */
public interface RedisEventCodec<T extends RedisEvent> {
    /**
     * Gets event type (staff.chat for example). codec and event type equality is required
     * @return event type
     */
    String type();

    /**
     * Gets event class
     * @return event class type
     */
    Class<T> eventClass();

    /**
     * Encode event to json
     * @param event redis event instance
     * @return encoded json string
     */
    String encodePayload(T event);

    /**
     * Decode json to redis event instance
     * @param json json event body
     * @return decoded redis event
     */
    T decodePayload(String json);
}
