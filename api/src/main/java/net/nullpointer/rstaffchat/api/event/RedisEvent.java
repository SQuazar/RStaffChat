package net.nullpointer.rstaffchat.api.event;

/**
 * Redis event
 */
public interface RedisEvent {
    /**
     * Redis event type
     * @return type of event
     */
    String type();
}
