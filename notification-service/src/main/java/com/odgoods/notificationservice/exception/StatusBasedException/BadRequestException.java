package com.odgoods.notificationservice.exception.StatusBasedException;

import com.odgoods.notificationservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
