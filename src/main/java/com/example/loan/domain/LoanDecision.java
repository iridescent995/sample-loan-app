package com.example.loan.domain;

import java.util.List;
import java.util.Objects;

public record LoanDecision(
        ApplicationStatus status,
        RiskBand riskBand,
        LoanOffer offer,
        List<RejectionReason> rejectionReasons
) {

    public static LoanDecision approved(RiskBand riskBand, LoanOffer offer) {
        return new LoanDecision(
                ApplicationStatus.APPROVED,
                Objects.requireNonNull(riskBand),
                Objects.requireNonNull(offer),
                List.of()
        );
    }

    public static LoanDecision rejected(List<RejectionReason> rejectionReasons) {
        return new LoanDecision(
                ApplicationStatus.REJECTED,
                null,
                null,
                List.copyOf(rejectionReasons)
        );
    }
}
