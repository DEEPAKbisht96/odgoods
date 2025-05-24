package com.odgoods.orderservice.exception.StatusBasedException;

import com.odgoods.orderservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
