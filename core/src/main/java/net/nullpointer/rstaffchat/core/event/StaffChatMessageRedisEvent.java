package net.nullpointer.rstaffchat.core.event;

import com.google.gson.Gson;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.nullpointer.rstaffchat.api.codec.RedisEventCodec;
import net.nullpointer.rstaffchat.api.event.RedisEvent;

import java.util.UUID;

/**
 * Staff chat message event. Trigger when someone send message to staff chat
 */
@Data
public class StaffChatMessageRedisEvent implements RedisEvent {
    /**
     * Event type
     */
    public static final String EVENT_TYPE = "staff.chat";

    private final String source;
    private final ChatInfo chatInfo;
    private final String groupPrefix;
    private final String senderName;
    private final UUID senderUuid;
    private final String message;

    /**
     * Event constructor
     * @param source where the message was sent from
     * @param chatInfo info about chat
     * @param groupPrefix sender group prefix
     * @param senderName sender name
     * @param senderUuid sender uuid
     * @param message message
     */
    public StaffChatMessageRedisEvent(String source, ChatInfo chatInfo,
                                      String groupPrefix, String senderName, UUID senderUuid, String message) {
        this.source = source;
        this.chatInfo = chatInfo;
        this.groupPrefix = groupPrefix;
        this.senderName = senderName;
        this.senderUuid = senderUuid;
        this.message = message;
    }

    /**
     * Event constructor
     * @param source where the message was sent from
     * @param chatInfo info about chat
     * @param senderName sender name
     * @param senderUuid sender uuid
     * @param message message
     */
    public StaffChatMessageRedisEvent(String source, ChatInfo chatInfo, String senderName, UUID senderUuid, String message) {
        this.source = source;
        this.chatInfo = chatInfo;
        this.groupPrefix = null;
        this.senderName = senderName;
        this.senderUuid = senderUuid;
        this.message = message;
    }

    /**
     * Gets event type
     * @return event type
     */
    @Override
    public String type() {
        return EVENT_TYPE;
    }

    /**
     * Staff chat message event codec
     */
    @RequiredArgsConstructor
    public static final class Codec implements RedisEventCodec<StaffChatMessageRedisEvent> {
        private final Gson gson;

        @Override
        public String type() {
            return EVENT_TYPE;
        }

        @Override
        public Class<StaffChatMessageRedisEvent> eventClass() {
            return StaffChatMessageRedisEvent.class;
        }

        @Override
        public String encodePayload(StaffChatMessageRedisEvent event) {
            return gson.toJson(event);
        }

        @Override
        public StaffChatMessageRedisEvent decodePayload(String json) {
            return gson.fromJson(json, StaffChatMessageRedisEvent.class);
        }
    }

    /**
     * Chat info data class
     * @param id chat id
     * @param permission permission for view chat messages
     */
    public record ChatInfo(String id, String permission) {
    }
}
