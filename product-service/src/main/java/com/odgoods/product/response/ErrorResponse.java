package com.odgoods.product.response;

import lombok.Data;

import java.util.Map;

@Data
public class ErrorResponse {

    private String message;
    private int status;
    private Map<String, String> errors;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public ErrorResponse(String message, int status, Map<String, String> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

}
