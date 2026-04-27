package com.lynx.fee_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleFeeNotFound(FeeNotFoundException ex) {

        ErrorResponse error = new ErrorResponse(
                "FEE_NOT_FOUND",
                ex.getMessage(),
                new HashMap<>()
        );

        Map<String, Object> body = new HashMap<>();
        body.put("error", error);

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {

        ErrorResponse error = new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "Something went wrong on the server.",
                new HashMap<>()
        );

        Map<String, Object> body = new HashMap<>();
        body.put("error", error);

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> details = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                details.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                "Validation failed",
                details
        );

        return ResponseEntity.badRequest().body(Map.of("error", error));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonError(HttpMessageNotReadableException ex) {

        String message = ex.getMostSpecificCause().getMessage();

        Map<String, String> details = new HashMap<>();
        details.put("error", message);

        ErrorResponse error = new ErrorResponse(
                "INVALID_JSON",
                "Malformed JSON",
                details
        );

        return ResponseEntity.badRequest().body(Map.of("error", error));
    }

}