package com.example.loan.service;

import com.example.loan.domain.EmploymentType;
import com.example.loan.domain.RiskBand;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class InterestRateCalculator {

    public static final BigDecimal BASE_RATE = new BigDecimal("12.00");

    private static final BigDecimal MEDIUM_RISK_PREMIUM = new BigDecimal("1.50");
    private static final BigDecimal HIGH_RISK_PREMIUM = new BigDecimal("3.00");
    private static final BigDecimal SELF_EMPLOYED_PREMIUM = new BigDecimal("1.00");
    private static final BigDecimal LARGE_LOAN_PREMIUM = new BigDecimal("0.50");
    private static final BigDecimal LARGE_LOAN_THRESHOLD = new BigDecimal("1000000.00");

    public BigDecimal calculate(RiskBand riskBand, EmploymentType employmentType, BigDecimal loanAmount) {
        BigDecimal rate = BASE_RATE
                .add(riskPremium(riskBand))
                .add(employmentPremium(employmentType))
                .add(loanSizePremium(loanAmount));

        return rate.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal riskPremium(RiskBand riskBand) {
        return switch (riskBand) {
            case LOW -> BigDecimal.ZERO;
            case MEDIUM -> MEDIUM_RISK_PREMIUM;
            case HIGH -> HIGH_RISK_PREMIUM;
        };
    }

    private BigDecimal employmentPremium(EmploymentType employmentType) {
        return switch (employmentType) {
            case SALARIED -> BigDecimal.ZERO;
            case SELF_EMPLOYED -> SELF_EMPLOYED_PREMIUM;
        };
    }

    private BigDecimal loanSizePremium(BigDecimal loanAmount) {
        if (loanAmount.compareTo(LARGE_LOAN_THRESHOLD) > 0) {
            return LARGE_LOAN_PREMIUM;
        }
        return BigDecimal.ZERO;
    }
}
