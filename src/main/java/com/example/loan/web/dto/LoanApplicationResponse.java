package com.example.loan.web.dto;

import com.example.loan.domain.ApplicationStatus;
import com.example.loan.domain.RejectionReason;
import com.example.loan.domain.RiskBand;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

public record LoanApplicationResponse(
        UUID applicationId,
        ApplicationStatus status,
        RiskBand riskBand,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        LoanOfferResponse offer,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        List<RejectionReason> rejectionReasons
) {
}
