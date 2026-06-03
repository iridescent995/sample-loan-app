package com.example.loan.web.dto;

import java.math.BigDecimal;

public record LoanOfferResponse(
        BigDecimal interestRate,
        Integer tenureMonths,
        BigDecimal emi,
        BigDecimal totalPayable
) {
}
