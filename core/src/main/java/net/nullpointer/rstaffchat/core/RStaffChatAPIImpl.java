package net.nullpointer.rstaffchat.core;

import com.google.gson.Gson;
import net.nullpointer.rstaffchat.api.Dispatcher;
import net.nullpointer.rstaffchat.api.RStaffChatAPI;
import net.nullpointer.rstaffchat.api.codec.RedisCodecRegistry;
import net.nullpointer.rstaffchat.api.redis.RedisPublisher;
import net.nullpointer.rstaffchat.api.redis.RedisSubscriber;
import net.nullpointer.rstaffchat.core.event.StaffChatMessageRedisEvent;

/**
 * API implementation
 */
public final class RStaffChatAPIImpl implements RStaffChatAPI {
    private final String channel;

    private final RedisCodecRegistry codecRegistry;
    private final RedisClientBootstrap clientBootstrap;
    private final RedisSubscriber subscriber;
    private RedisPublisher publisher;

    RStaffChatAPIImpl(
            String redisUri,
            String channel,
            Dispatcher dispatcher,
            Gson gson
    ) {
        this.channel = channel;
        this.codecRegistry = new RedisCodecRegistryImpl();
        this.subscriber = new RedisSubscriberImpl(codecRegistry, dispatcher);
        this.clientBootstrap = new RedisClientBootstrap(
                redisUri,
                channel,
                gson,
                (RedisSubscriberImpl) subscriber
        );

        codecRegistry.register(new StaffChatMessageRedisEvent.Codec(gson));
    }

    @Override
    public void init() {
        clientBootstrap.start();
        this.publisher = new RedisPublisherImpl(this.clientBootstrap.getPublishConnection(), codecRegistry, channel);
    }

    @Override
    public void shutdown() {
        clientBootstrap.stop();
    }

    @Override
    public RedisCodecRegistry codecRegistry() {
        return codecRegistry;
    }

    @Override
    public RedisPublisher publisher() {
        return publisher;
    }

    @Override
    public RedisSubscriber subscriber() {
        return subscriber;
    }

    /**
     * Gets builder
     * @return API builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * API Builder
     */
    public static final class Builder {
        private String redisUri;
        private Dispatcher dispatcher = Runnable::run;
        private String channel;
        private Gson gson = new Gson();

        /**
         * Sets redis uri
         * @param uri redis uri
         * @return builder instance
         */
        public Builder redisUri(String uri) {
            this.redisUri = uri;
            return this;
        }

        /**
         * Sets dispatcher. Default - in current thread
         * @param dispatcher event handling dispatcher
         * @return builder instance
         */
        public Builder dispatcher(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        /**
         * Sets redis event channel
         * @param channel event channel
         * @return builder instance
         */
        public Builder channel(String channel) {
            this.channel = channel;
            return this;
        }

        /**
         * Sets google gson
         * @param gson gson instance
         * @return builder instance
         */
        public Builder gson(Gson gson) {
            this.gson = gson;
            return this;
        }

        /**
         * Create API instance from current Builder
         * @return API instance
         */
        public RStaffChatAPI build() {
            return new RStaffChatAPIImpl(
                    redisUri,
                    channel,
                    dispatcher,
                    gson
            );
        }
    }
}
