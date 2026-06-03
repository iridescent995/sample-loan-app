package com.example.loan.domain;

import com.example.loan.web.dto.CreateLoanApplicationRequest;

import java.time.Instant;
import java.util.UUID;

public record StoredLoanApplication(
        UUID applicationId,
        CreateLoanApplicationRequest request,
        LoanDecision decision,
        Instant createdAt
) {
}
