package com.odgoods.authservice.common.exception.StatusBasedException;

import com.odgoods.authservice.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
