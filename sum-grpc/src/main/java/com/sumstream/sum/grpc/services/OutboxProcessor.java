package com.sumstream.sum.grpc.services;

import com.sumstream.sum.grpc.entity.OutboxMessage;
import com.sumstream.sum.grpc.repository.OutboxRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;


@Service
public class OutboxProcessor {

    private static final String TOPIC = "sum-topic";

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxProcessor(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 30000) // Every 30 seconds
    public void processOutbox() {
        List<OutboxMessage> pending = outboxRepository.findUnprocessedMessages();

        for (OutboxMessage message : pending) {
            OutboxMessage msg;
            try {
                processMessage(message);

                msg = new OutboxMessage(
                        message.id(),
                        message.type(),
                        message.content(),
                        message.occurredOnUTC(),
                        Instant.now(),
                        null
                );

            } catch (Exception e) {
                msg = new OutboxMessage(
                        message.id(),
                        message.type(),
                        message.content(),
                        message.occurredOnUTC(),
                        null,
                        e.getMessage()
                );
            }

            outboxRepository.save(msg);
        }
    }

    private void processMessage(OutboxMessage msg) {
        kafkaTemplate.send(TOPIC, msg.type(), msg.content());
    }

}
