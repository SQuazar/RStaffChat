package net.nullpointer.rstaffchat.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nullpointer.rstaffchat.api.Dispatcher;
import net.nullpointer.rstaffchat.api.RStaffChatAPI;
import net.nullpointer.rstaffchat.api.codec.RedisCodecRegistry;
import net.nullpointer.rstaffchat.api.codec.RedisEventCodec;
import net.nullpointer.rstaffchat.api.event.RedisEvent;
import net.nullpointer.rstaffchat.api.event.RedisEventHandler;
import net.nullpointer.rstaffchat.api.redis.RedisSubscriber;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
class RedisSubscriberImpl implements RedisSubscriber {
    private final RedisCodecRegistry codecRegistry;
    private final Dispatcher dispatcher;

    public RedisSubscriberImpl(RedisCodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
        this.dispatcher = Runnable::run;
    }

    private final Map<String, RedisEventHandler<?>> handlers = new HashMap<>();

    @Override
    public <T extends RedisEvent> void subscribe(String type, Class<T> clazz, RedisEventHandler<T> handler) {
        handlers.put(type, handler);
    }

    public void onMessage(RedisEventEnvelope event) {
        if (event.getSource().equals(RStaffChatAPI.ID)) return;
        RedisEventCodec<RedisEvent> codec = codecRegistry.get(event.getType());
        if (codec == null) {
            log.warn("Cannot find codec for event type {}", event.getType());
            return;
        }

        RedisEvent e = codec.decodePayload(event.getPayload());
        RedisEventHandler<RedisEvent> handler = (RedisEventHandler<RedisEvent>) handlers.get(event.getType());
        if (handler == null) return;

        dispatcher.dispatch(() -> handler.handle(e));
    }
}
