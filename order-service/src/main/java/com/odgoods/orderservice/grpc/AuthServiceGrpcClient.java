package com.odgoods.orderservice.grpc;

import auth.AuthResponse;
import auth.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AuthServiceGrpcClient {

    private final AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    public AuthServiceGrpcClient(
            @Value("${auth.service.address:auth-service}") String serverAddress,
            @Value("${auth.service.grpc.port:9000}") String serverPort
    ) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(serverAddress, Integer.parseInt(serverPort))
                .usePlaintext()
                .build();

        authServiceBlockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public AuthResponse isUserValid(long userId) {
        log.info("AuthResponse :: isUserValid: {}", userId);
        return authServiceBlockingStub.isUserValid(auth.AuthRequest.newBuilder().setId(userId).build());
    }
}
