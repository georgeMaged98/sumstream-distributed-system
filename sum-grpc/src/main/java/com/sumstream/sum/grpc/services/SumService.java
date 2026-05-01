package com.sumstream.sum.grpc.services;

import com.sumstream.sum.grpc.entity.OutboxMessage;
import com.sumstream.sum.grpc.entity.Sum;
import com.sumstream.sum.grpc.proto.SumRequest;
import com.sumstream.sum.grpc.repository.SumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sumstream.sum.grpc.repository.OutboxRepository;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SumService {

    private SumRepository sumRepository;
    private OutboxRepository outboxRepository;

    public SumService(SumRepository sumRepository, OutboxRepository outboxRepository) {
        this.sumRepository = sumRepository;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public long addTwoNumbers(SumRequest sumRequest) {

        long twoNumbersSum = sumRequest.getNum1() + sumRequest.getNum2();
        // To achieve outbox pattern and have multiple statements in transactions.
        Sum sum = Sum.of(sumRequest.getNum1(), sumRequest.getNum2(), twoNumbersSum);
        Sum savedSum = sumRepository.save(sum);

        OutboxMessage outboxMessage = OutboxMessage.of("NumbersSum", sumRequest);
        outboxRepository.save(outboxMessage);

        return savedSum.id();
    }
}
