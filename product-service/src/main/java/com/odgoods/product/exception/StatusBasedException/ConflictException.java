package com.odgoods.product.exception.StatusBasedException;

import com.odgoods.product.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
