package com.example.loan.service;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmiCalculatorTest {

    private static final Logger log = LoggerFactory.getLogger(EmiCalculatorTest.class);

    private final EmiCalculator calculator = new EmiCalculator();

    @Test
    void calculatesEmiUsingRoundedFinancialScale() {
        BigDecimal expectedEmi = new BigDecimal("16607.15");
        BigDecimal emi = calculator.calculate(
                new BigDecimal("500000.00"),
                new BigDecimal("12.00"),
                36
        );

        logCheck("EMI for 500000.00 at 12.00% over 36 months", expectedEmi, emi);
        assertEquals(expectedEmi, emi);
    }

    @Test
    void calculatesStraightLineEmiWhenInterestRateIsZero() {
        BigDecimal expectedEmi = new BigDecimal("13888.89");
        BigDecimal emi = calculator.calculate(
                new BigDecimal("500000.00"),
                BigDecimal.ZERO,
                36
        );

        logCheck("EMI for 500000.00 at 0% over 36 months", expectedEmi, emi);
        assertEquals(expectedEmi, emi);
    }

    private void logCheck(String check, Object expected, Object actual) {
        log.info("{}: expected={}, actual={}", check, expected, actual);
    }
}
