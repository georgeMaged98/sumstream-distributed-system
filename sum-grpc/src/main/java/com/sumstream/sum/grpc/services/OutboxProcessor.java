package com.sumstream.sum.grpc.services;

import com.sumstream.sum.grpc.entity.OutboxMessage;
import com.sumstream.sum.grpc.repository.OutboxRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;

    public OutboxProcessor(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Scheduled(fixedDelay = 30000) // Every 30 seconds
    public void processOutbox() {
        List<OutboxMessage> pending = outboxRepository.findUnprocessedMessages();

        for (OutboxMessage message : pending) {
            OutboxMessage msg = null;
            try {
                // 1. Process message (your logic here)
                processMessage(message);

                // 2. Save success state
                msg = new OutboxMessage(
                        message.id(),
                        message.type(),
                        message.content(),
                        message.occurredOnUTC(),
                        Instant.now(),       // processed_on_utc
                        null                 // error
                );

            } catch (Exception e) {
                // Save error
                msg = new OutboxMessage(
                        message.id(),
                        message.type(),
                        message.content(),
                        message.occurredOnUTC(),
                        null,
                        e.getMessage()
                );

            } finally {
                outboxRepository.save(msg);
            }
        }
    }

    private void processMessage(OutboxMessage msg) {
        // send to Kafka
        System.out.println("Processing outbox: " + msg.type());
    }

}
