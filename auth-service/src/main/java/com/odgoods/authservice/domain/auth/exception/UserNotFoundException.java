package com.odgoods.authservice.domain.auth.exception;

import com.odgoods.authservice.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message, HttpStatus status) {
        super("User not found", HttpStatus.NOT_FOUND);
    }

}
