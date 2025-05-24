package com.odgoods.authservice.common.exception.StatusBasedException;

import com.odgoods.authservice.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
