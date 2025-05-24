package com.odgoods.notificationservice.exception.StatusBasedException;

import com.odgoods.notificationservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
