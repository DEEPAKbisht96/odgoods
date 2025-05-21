package com.odgoods.authservice.grpc;

import auth.AuthServiceGrpc;
import com.odgoods.authservice.domain.auth.service.AuthService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;


@GrpcService
@RequiredArgsConstructor
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService authService;

    @Override
    public void isUserValid(auth.AuthRequest request, StreamObserver<auth.AuthResponse> responseObserver){

        long userId = request.getId();
        boolean isValid = authService.getUserById(userId) != null;

        auth.AuthResponse response = auth.AuthResponse.newBuilder()
                .setId(userId)
                .setIsValid(isValid)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
