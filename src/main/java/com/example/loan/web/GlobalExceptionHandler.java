package com.example.loan.web;

import com.example.loan.web.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .toList();

        return badRequest("Validation failed", errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationErrorResponse> handleUnreadableBody(HttpMessageNotReadableException exception) {
        return badRequest(
                "Invalid request body",
                List.of("Request body must be valid JSON with supported field values")
        );
    }

    private ResponseEntity<ValidationErrorResponse> badRequest(String message, List<String> errors) {
        return ResponseEntity.badRequest()
                .body(new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), message, errors));
    }
}
