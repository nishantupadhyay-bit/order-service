package com.assignment.orderservice.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcServerConfiguration {

    @Value("${grpc.server.port:9092}")
    private int grpcPort;

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public Server grpcServer(OrderGrpcService orderGrpcService) {
        return ServerBuilder.forPort(grpcPort)
                .addService(orderGrpcService)
                .addService(ProtoReflectionService.newInstance())
                .build();
    }
}