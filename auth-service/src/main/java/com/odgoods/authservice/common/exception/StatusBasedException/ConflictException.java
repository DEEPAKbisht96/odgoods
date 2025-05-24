package com.odgoods.authservice.common.exception.StatusBasedException;

import com.odgoods.authservice.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
