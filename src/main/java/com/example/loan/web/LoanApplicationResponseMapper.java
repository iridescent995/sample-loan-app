package com.example.loan.web;

import com.example.loan.domain.ApplicationStatus;
import com.example.loan.domain.LoanDecision;
import com.example.loan.domain.LoanOffer;
import com.example.loan.domain.RejectionReason;
import com.example.loan.domain.StoredLoanApplication;
import com.example.loan.web.dto.LoanApplicationResponse;
import com.example.loan.web.dto.LoanOfferResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanApplicationResponseMapper {

    public LoanApplicationResponse toResponse(StoredLoanApplication application) {
        LoanDecision decision = application.decision();

        return new LoanApplicationResponse(
                application.applicationId(),
                decision.status(),
                decision.riskBand(),
                toOfferResponse(decision.offer()),
                toRejectionReasons(decision)
        );
    }

    private LoanOfferResponse toOfferResponse(LoanOffer offer) {
        if (offer == null) {
            return null;
        }

        return new LoanOfferResponse(
                offer.interestRate(),
                offer.tenureMonths(),
                offer.emi(),
                offer.totalPayable()
        );
    }

    private List<RejectionReason> toRejectionReasons(LoanDecision decision) {
        if (decision.status() == ApplicationStatus.APPROVED) {
            return null;
        }

        return decision.rejectionReasons();
    }
}
