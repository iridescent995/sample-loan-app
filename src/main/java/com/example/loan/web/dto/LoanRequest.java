package com.example.loan.web.dto;

import com.example.loan.domain.LoanPurpose;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record LoanRequest(
        @NotNull(message = "loan.amount is required")
        @DecimalMin(value = "10000.00", message = "loan.amount must be at least 10000")
        @DecimalMax(value = "5000000.00", message = "loan.amount must be at most 5000000")
        BigDecimal amount,

        @NotNull(message = "loan.tenureMonths is required")
        @Min(value = 6, message = "loan.tenureMonths must be at least 6")
        @Max(value = 360, message = "loan.tenureMonths must be at most 360")
        Integer tenureMonths,

        @NotNull(message = "loan.purpose is required")
        LoanPurpose purpose
) {
}
