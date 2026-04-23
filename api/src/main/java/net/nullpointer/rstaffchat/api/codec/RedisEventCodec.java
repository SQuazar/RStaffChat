package net.nullpointer.rstaffchat.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public interface RedisEventCodec<T extends RedisEvent> {
    String type();

    Class<T> eventClass();

    JsonElement encodePayload(T event, Gson gson);

    T decodePayload(JsonElement json, Gson gson);
}
