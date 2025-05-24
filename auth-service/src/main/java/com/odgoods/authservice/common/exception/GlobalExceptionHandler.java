package com.odgoods.authservice.common.exception;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.odgoods.authservice.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Validation errors from @Valid / @Validated
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> {
                    assert err.getDefaultMessage() != null;
                    return Map.of(
                            "field", err.getField(),
                            "message", err.getDefaultMessage()
                    );
                })
                .toList();

        return ResponseEntity.badRequest().body(Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "errors", errors
        ));
    }

    // Invalid enum or JSON format error
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormat(InvalidFormatException ex) {
        String fieldName = ex.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .reduce((first, second) -> second)
                .orElse("unknown");

        String targetType = ex.getTargetType().getSimpleName();

        String message = String.format("Invalid value for field '%s'. Allowed values: %s",
                fieldName,
                targetType.equals("CountryCode") || targetType.equals("ProductCategory")
                        ? Arrays.toString(ex.getTargetType().getEnumConstants())
                        : "Check the expected format.");

        return ResponseEntity.badRequest().body(Map.of(
                "status", HttpStatus.BAD_REQUEST.value(),
                "error", message
        ));
    }


    // custom business exceptions
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {

        logger.error("BaseException: {}", e.getMessage(), e); // logs stack trace

        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorResponse(e.getMessage(), e.getStatus().value()));
    }


    // Handle other uncaught exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {

        logger.error("BaseException: {}", ex.getMessage(), ex); // logs stack trace

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Something went wrong", 500));
    }
}
