package com.odgoods.authservice.grpc;

import auth.AuthServiceGrpc;
import com.odgoods.authservice.domain.auth.dto.UserResponse;
import com.odgoods.authservice.domain.auth.model.Role;
import com.odgoods.authservice.domain.auth.service.AuthService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;


@Slf4j
@GrpcService
@RequiredArgsConstructor
public class AuthGrpcService extends AuthServiceGrpc.AuthServiceImplBase {

    private final AuthService authService;

    @Override
    public void isUserValid(auth.AuthRequest request, StreamObserver<auth.AuthResponse> responseObserver){

        long userId = request.getId();
        UserResponse user = authService.getUserById(userId);
        boolean isValid = user != null;

        String userRole = (user != null) ? user.role().name() : Role.USER.name();

        log.info("isUserValid: id={}, role={}, isValid={}", userId, userRole, isValid);

        auth.AuthResponse response = auth.AuthResponse.newBuilder()
                .setId(userId)
                .setRole(userRole)
                .setIsValid(isValid)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
