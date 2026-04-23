package net.nullpointer.rstaffchat.core;

import io.lettuce.core.api.StatefulRedisConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nullpointer.rstaffchat.api.RStaffChatAPI;
import net.nullpointer.rstaffchat.api.codec.RedisCodecRegistry;
import net.nullpointer.rstaffchat.api.codec.RedisEventCodec;
import net.nullpointer.rstaffchat.api.event.RedisEvent;
import net.nullpointer.rstaffchat.api.redis.RedisPublisher;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
class RedisPublisherImpl implements RedisPublisher {
    private final StatefulRedisConnection<String, RedisEventEnvelope> publishConnection;
    private final RedisCodecRegistry codecRegistry;
    private final String channel;

    @Override
    public <T extends RedisEvent> void publish(T event) {
        RedisEventCodec<T> codec = codecRegistry.get(event.type());
        if (codec == null) {
            log.warn("Cannot find codec for event type {}", event.type());
            return;
        }
        publishConnection.async().publish(channel, new RedisEventEnvelope(
                UUID.randomUUID(),
                RStaffChatAPI.ID,
                event.type(),
                codec.encodePayload(event)
        ));
    }
}
