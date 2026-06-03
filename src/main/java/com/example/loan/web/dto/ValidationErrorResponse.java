package com.example.loan.web.dto;

import java.util.List;

public record ValidationErrorResponse(
        int status,
        String message,
        List<String> errors
) {
}
