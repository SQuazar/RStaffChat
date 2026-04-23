package net.nullpointer.rstaffchat.api;

@FunctionalInterface
public interface RedisEventHandler<T extends RedisEvent> {
    void handle(T event);
}
