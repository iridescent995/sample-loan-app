package com.example.loan.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
public class EmiCalculator {

    private static final int MONEY_SCALE = 2;
    private static final int RATE_SCALE = 10;
    private static final MathContext EMI_CONTEXT = new MathContext(20, RoundingMode.HALF_UP);
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
    private static final BigDecimal MONTHLY_PERCENT_DIVISOR = new BigDecimal("1200");

    public BigDecimal calculate(BigDecimal principal, BigDecimal annualRatePercent, int tenureMonths) {
            BigDecimal monthlyRate = annualRatePercent.divide(MONTHLY_PERCENT_DIVISOR, RATE_SCALE, ROUNDING);
            if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
                return principal.divide(BigDecimal.valueOf(tenureMonths), MONEY_SCALE, ROUNDING);
            }

        BigDecimal rateFactor = BigDecimal.ONE.add(monthlyRate).pow(tenureMonths, EMI_CONTEXT);
        BigDecimal numerator = principal.multiply(monthlyRate, EMI_CONTEXT).multiply(rateFactor, EMI_CONTEXT);
        BigDecimal denominator = rateFactor.subtract(BigDecimal.ONE, EMI_CONTEXT);

        return numerator.divide(denominator, MONEY_SCALE, ROUNDING);
    }



}


