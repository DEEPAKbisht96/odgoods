package com.odgoods.authservice.common.exception.StatusBasedException;

import com.odgoods.authservice.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
