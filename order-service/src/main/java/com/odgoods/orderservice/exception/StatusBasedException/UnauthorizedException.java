package com.odgoods.orderservice.exception.StatusBasedException;

import com.odgoods.orderservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
