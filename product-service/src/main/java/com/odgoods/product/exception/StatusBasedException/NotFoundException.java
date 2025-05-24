package com.odgoods.product.exception.StatusBasedException;

import com.odgoods.product.exception.BaseException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
