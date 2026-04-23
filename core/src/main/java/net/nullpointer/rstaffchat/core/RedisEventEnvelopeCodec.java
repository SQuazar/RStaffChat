package net.nullpointer.rstaffchat.core;

import com.google.gson.Gson;
import com.google.gson.Strictness;
import com.google.gson.stream.JsonReader;
import io.lettuce.core.codec.RedisCodec;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
class RedisEventEnvelopeCodec implements RedisCodec<String, RedisEventEnvelope> {
    private static final Charset UTF8 = StandardCharsets.UTF_8;

    private final Gson gson;

    @Override
    public String decodeKey(ByteBuffer bytes) {
        byte[] arr = new byte[bytes.remaining()];
        bytes.get(arr);
        return new String(arr, UTF8);
    }

    @Override
    public RedisEventEnvelope decodeValue(ByteBuffer bytes) {
        byte[] arr = new byte[bytes.remaining()];
        bytes.get(arr);
        try (ByteArrayInputStream is = new ByteArrayInputStream(arr);
             JsonReader reader = new JsonReader(new InputStreamReader(is, UTF8))) {
            reader.setStrictness(Strictness.LENIENT);
            RedisEventEnvelope event = gson.fromJson(reader, RedisEventEnvelope.class);
            if (event == null || event.getType() == null)
                throw new IllegalStateException("Invalid Event Message");
            return event;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to decode Redis Event Message", e);
        }
    }

    @Override
    public ByteBuffer encodeKey(String key) {
        return UTF8.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(RedisEventEnvelope value) {
        return UTF8.encode(gson.toJson(value));
    }
}
