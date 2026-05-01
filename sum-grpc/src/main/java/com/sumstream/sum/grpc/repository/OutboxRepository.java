package com.sumstream.sum.grpc.repository;

import com.sumstream.sum.grpc.entity.OutboxMessage;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Optional;


@Repository
public interface OutboxRepository extends CrudRepository<OutboxMessage, Long> {

    @Query("SELECT id, type, content, occurred_on_utc FROM outbox_message WHERE processed_on_utc IS NULL ORDER BY occurred_on_utc ASC  LIMIT 10 FOR UPDATE SKIP LOCKED;")
    List<OutboxMessage> findUnprocessedMessages();
}
