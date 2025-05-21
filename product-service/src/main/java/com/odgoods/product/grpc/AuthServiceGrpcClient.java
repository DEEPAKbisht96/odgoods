package com.odgoods.product.grpc;

import auth.AuthResponse;
import auth.AuthServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;

public class AuthServiceGrpcClient {

    private final AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    public AuthServiceGrpcClient(
            @Value("${auth.service.address:localhost}") String serverAddress,
            @Value("${auth.service.grpc.port:9001}") String serverPost
    ) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(serverAddress, Integer.parseInt(serverPost))
                .usePlaintext()
                .build();

        authServiceBlockingStub = AuthServiceGrpc.newBlockingStub(channel);
    }

    public AuthResponse isUserValid(long userId) {
        return authServiceBlockingStub.isUserValid(auth.AuthRequest.newBuilder().setId(userId).build());
    }
}
