package com.sumstream.sum.grpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SumGrpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SumGrpcApplication.class, args);
    }

}
