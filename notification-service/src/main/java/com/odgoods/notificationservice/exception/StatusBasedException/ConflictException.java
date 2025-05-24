package com.odgoods.notificationservice.exception.StatusBasedException;

import com.odgoods.notificationservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
