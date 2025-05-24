package com.odgoods.orderservice.exception.StatusBasedException;

import com.odgoods.orderservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
