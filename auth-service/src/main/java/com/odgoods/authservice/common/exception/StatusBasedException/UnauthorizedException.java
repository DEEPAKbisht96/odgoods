package com.odgoods.authservice.common.exception.StatusBasedException;

import com.odgoods.authservice.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
