package com.odgoods.notificationservice.exception.StatusBasedException;

import com.odgoods.notificationservice.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
