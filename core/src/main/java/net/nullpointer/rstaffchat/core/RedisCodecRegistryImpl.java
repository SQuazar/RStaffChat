package net.nullpointer.rstaffchat.core;

import net.nullpointer.rstaffchat.api.codec.RedisCodecRegistry;
import net.nullpointer.rstaffchat.api.codec.RedisEventCodec;
import net.nullpointer.rstaffchat.api.event.RedisEvent;

import java.util.HashMap;
import java.util.Map;

class RedisCodecRegistryImpl implements RedisCodecRegistry {
    private final Map<String, RedisEventCodec<?>> codecs = new HashMap<>();

    @Override
    public <T extends RedisEvent> void register(RedisEventCodec<T> codec) {
        codecs.put(codec.type(), codec);
    }

    @Override
    public <T extends RedisEvent> RedisEventCodec<T> get(String type) {
        return (RedisEventCodec<T>) codecs.get(type);
    }
}
