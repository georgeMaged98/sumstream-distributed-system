package com.sumstream.sum.grpc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("sum")
public record Sum(@Id Long id, int num1, int num2, long sum) {

    public static Sum of(int num1, int num2, long sum) {
        return new Sum(null, num1, num2, sum);
    }
}
