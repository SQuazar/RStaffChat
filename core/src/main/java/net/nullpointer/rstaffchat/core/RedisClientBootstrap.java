package net.nullpointer.rstaffchat.core;

import com.google.gson.Gson;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import lombok.Getter;

class RedisClientBootstrap {
    private final RedisClient client;
    private @Getter StatefulRedisConnection<String, RedisEventEnvelope> publishConnection;
    private @Getter StatefulRedisPubSubConnection<String, RedisEventEnvelope> subscriberConnection;

    private final Gson gson;
    private final RedisSubscriberImpl subscriber;
    private final String channel;

    public RedisClientBootstrap(
            String redisUri,
            String channel,
            Gson gson,
            RedisSubscriberImpl subscriber
    ) {
        this.client = RedisClient.create(redisUri);
        this.gson = gson;
        this.subscriber = subscriber;
        this.channel = channel;
    }

    public void start() {
        RedisCodec<String, RedisEventEnvelope> codec = new RedisEventEnvelopeCodec(gson);
        this.publishConnection = client.connect(codec);
        this.subscriberConnection = client.connectPubSub(codec);

        subscriberConnection.addListener(new RedisPubSubAdapter<>() {
            @Override
            public void message(String channel, RedisEventEnvelope event) {
                subscriber.onMessage(event);
            }
        });
        subscriberConnection.async().subscribe(channel);
    }

    public void stop() {
        if (subscriberConnection != null) subscriberConnection.close();
        if (publishConnection != null) publishConnection.close();
        client.shutdown();
    }
}
