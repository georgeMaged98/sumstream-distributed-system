package com.sumstream.sum.grpc.entity;

import java.time.Instant;
import com.sumstream.sum.grpc.utils.JsonUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("outbox_message")
public record OutboxMessage(
        @Id
        Long id,
        String type,
        String content,
        @Column("occurred_on_utc") Instant occurredOnUTC,
        @Column("processed_on_utc") Instant processedOnUTC,
        String error
) {
    public static OutboxMessage of(String eventType, Object payload) {

        // Factory method for new messages
        return new OutboxMessage(null, eventType, JsonUtils.toJson(payload), Instant.now(), null, null);
    }
}
