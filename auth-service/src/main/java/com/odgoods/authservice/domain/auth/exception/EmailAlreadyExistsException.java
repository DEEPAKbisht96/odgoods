package com.odgoods.authservice.domain.auth.exception;

import com.odgoods.authservice.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends BaseException {
    
    // Conflict exception - 400
    public EmailAlreadyExistsException(String message, HttpStatus status) {
        super("Email already exists", HttpStatus.CONFLICT);
    }
}
