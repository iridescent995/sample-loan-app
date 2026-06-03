package com.example.loan.service;

import com.example.loan.domain.LoanDecision;
import com.example.loan.domain.LoanOffer;
import com.example.loan.domain.RejectionReason;
import com.example.loan.domain.RiskBand;
import com.example.loan.web.dto.ApplicantRequest;
import com.example.loan.web.dto.CreateLoanApplicationRequest;
import com.example.loan.web.dto.LoanRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanEvaluationService {

    private static final BigDecimal AGE_TENURE_LIMIT_YEARS = new BigDecimal("65");
    private static final BigDecimal MONTHS_PER_YEAR = new BigDecimal("12");
    private static final BigDecimal SIXTY_PERCENT = new BigDecimal("0.60");
    private static final BigDecimal FIFTY_PERCENT = new BigDecimal("0.50");

    private final EmiCalculator emiCalculator;
    private final RiskBandClassifier riskBandClassifier;
    private final InterestRateCalculator interestRateCalculator;

    public LoanEvaluationService(
            EmiCalculator emiCalculator,
            RiskBandClassifier riskBandClassifier,
            InterestRateCalculator interestRateCalculator
    ) {
        this.emiCalculator = emiCalculator;
        this.riskBandClassifier = riskBandClassifier;
        this.interestRateCalculator = interestRateCalculator;
    }

    public LoanDecision evaluate(CreateLoanApplicationRequest request) {
        return evaluate(request.applicant(), request.loan());
    }

    public LoanDecision evaluate(ApplicantRequest applicant, LoanRequest loan) {
        List<RejectionReason> rejectionReasons = new ArrayList<>();

        if (applicant.creditScore() < 600) {
            rejectionReasons.add(RejectionReason.CREDIT_SCORE_BELOW_MINIMUM);
        }
        if (ageTenureLimitExceeded(applicant.age(), loan.tenureMonths())) {
            rejectionReasons.add(RejectionReason.AGE_TENURE_LIMIT_EXCEEDED);
        }
        if (baseEmiExceedsSixtyPercent(applicant.monthlyIncome(), loan)) {
            rejectionReasons.add(RejectionReason.EMI_EXCEEDS_60_PERCENT);
        }
        if (!rejectionReasons.isEmpty()) {
            return LoanDecision.rejected(rejectionReasons);
        }

        RiskBand riskBand = riskBandClassifier.classify(applicant.creditScore());
        BigDecimal interestRate = interestRateCalculator.calculate(
                riskBand,
                applicant.employmentType(),
                loan.amount()
        );
        BigDecimal emi = emiCalculator.calculate(loan.amount(), interestRate, loan.tenureMonths());

        if (emi.compareTo(percentOf(applicant.monthlyIncome(), FIFTY_PERCENT)) > 0) {
            return LoanDecision.rejected(List.of(RejectionReason.EMI_EXCEEDS_50_PERCENT));
        }

        BigDecimal totalPayable = emi
                .multiply(BigDecimal.valueOf(loan.tenureMonths()))
                .setScale(2, RoundingMode.HALF_UP);

        return LoanDecision.approved(
                riskBand,
                new LoanOffer(interestRate, loan.tenureMonths(), emi, totalPayable)
        );
    }

    private boolean ageTenureLimitExceeded(int age, int tenureMonths) {
        BigDecimal tenureYears = BigDecimal.valueOf(tenureMonths)
                .divide(MONTHS_PER_YEAR, 10, RoundingMode.HALF_UP);
        BigDecimal ageAtLoanEnd = BigDecimal.valueOf(age).add(tenureYears);

        return ageAtLoanEnd.compareTo(AGE_TENURE_LIMIT_YEARS) > 0;
    }

    private boolean baseEmiExceedsSixtyPercent(BigDecimal monthlyIncome, LoanRequest loan) {
        BigDecimal emi = emiCalculator.calculate(
                loan.amount(),
                InterestRateCalculator.BASE_RATE,
                loan.tenureMonths()
        );

        return emi.compareTo(percentOf(monthlyIncome, SIXTY_PERCENT)) > 0;
    }

    private BigDecimal percentOf(BigDecimal amount, BigDecimal percent) {
        return amount.multiply(percent).setScale(2, RoundingMode.HALF_UP);
    }
}
