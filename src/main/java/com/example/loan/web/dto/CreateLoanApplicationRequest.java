package com.example.loan.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record CreateLoanApplicationRequest(
        @Valid
        @NotNull(message = "applicant is required")
        ApplicantRequest applicant,

        @Valid
        @NotNull(message = "loan is required")
        LoanRequest loan
) {
}
