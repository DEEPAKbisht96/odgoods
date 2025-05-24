package com.odgoods.orderservice.exception.StatusBasedException;

import com.odgoods.orderservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
