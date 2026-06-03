package com.example.loan.service;

import com.example.loan.domain.ApplicationStatus;
import com.example.loan.domain.EmploymentType;
import com.example.loan.domain.LoanPurpose;
import com.example.loan.domain.RejectionReason;
import com.example.loan.domain.RiskBand;
import com.example.loan.web.dto.ApplicantRequest;
import com.example.loan.web.dto.LoanRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LoanEvaluationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(LoanEvaluationServiceTest.class);

    private final LoanEvaluationService service = new LoanEvaluationService(
            new EmiCalculator(),
            new RiskBandClassifier(),
            new InterestRateCalculator()
    );

    @Test
    void approvesEligibleApplicationAndGeneratesSingleOfferForRequestedTenure() {
        var decision = service.evaluate(
                applicant(30, "75000.00", EmploymentType.SALARIED, 720),
                loan("500000.00", 36)
        );

        logCheck("Approval status", ApplicationStatus.APPROVED, decision.status());
        logCheck("Risk band for credit score 720", RiskBand.MEDIUM, decision.riskBand());
        logCheck("Interest rate for medium risk salaried applicant", new BigDecimal("13.50"), decision.offer().interestRate());
        logCheck("Requested tenure in generated offer", 36, decision.offer().tenureMonths());
        logCheck("Final EMI for generated offer", new BigDecimal("16967.64"), decision.offer().emi());
        logCheck("Total payable for generated offer", new BigDecimal("610835.04"), decision.offer().totalPayable());
        assertEquals(ApplicationStatus.APPROVED, decision.status());
        assertEquals(RiskBand.MEDIUM, decision.riskBand());
        assertNotNull(decision.offer());
        assertEquals(new BigDecimal("13.50"), decision.offer().interestRate());
        assertEquals(36, decision.offer().tenureMonths());
        assertEquals(new BigDecimal("16967.64"), decision.offer().emi());
        assertEquals(new BigDecimal("610835.04"), decision.offer().totalPayable());
        assertEquals(List.of(), decision.rejectionReasons());
    }

    @Test
    void rejectsApplicationWithMultipleEligibilityFailures() {
        var decision = service.evaluate(
                applicant(60, "10000.00", EmploymentType.SALARIED, 550),
                loan("1000000.00", 120)
        );

        List<RejectionReason> expectedReasons = List.of(
                RejectionReason.CREDIT_SCORE_BELOW_MINIMUM,
                RejectionReason.AGE_TENURE_LIMIT_EXCEEDED,
                RejectionReason.EMI_EXCEEDS_60_PERCENT
        );

        logCheck("Rejection status for multiple eligibility failures", ApplicationStatus.REJECTED, decision.status());
        logCheck("Rejection reasons for low score, age-tenure, and EMI limit", expectedReasons, decision.rejectionReasons());
        assertEquals(ApplicationStatus.REJECTED, decision.status());
        assertNull(decision.riskBand());
        assertNull(decision.offer());
        assertEquals(expectedReasons, decision.rejectionReasons());
    }

    @Test
    void rejectsWhenFinalOfferEmiExceedsFiftyPercentOfIncome() {
        var decision = service.evaluate(
                applicant(30, "30000.00", EmploymentType.SELF_EMPLOYED, 600),
                loan("500000.00", 36)
        );

        List<RejectionReason> expectedReasons = List.of(RejectionReason.EMI_EXCEEDS_50_PERCENT);

        logCheck("Rejection status when final offer EMI is above 50% income", ApplicationStatus.REJECTED, decision.status());
        logCheck("Rejection reason for final offer EMI limit", expectedReasons, decision.rejectionReasons());
        assertEquals(ApplicationStatus.REJECTED, decision.status());
        assertNull(decision.riskBand());
        assertNull(decision.offer());
        assertEquals(expectedReasons, decision.rejectionReasons());
    }

    private ApplicantRequest applicant(
            int age,
            String monthlyIncome,
            EmploymentType employmentType,
            int creditScore
    ) {
        return new ApplicantRequest(
                "Test Applicant",
                age,
                new BigDecimal(monthlyIncome),
                employmentType,
                creditScore
        );
    }

    private LoanRequest loan(String amount, int tenureMonths) {
        return new LoanRequest(
                new BigDecimal(amount),
                tenureMonths,
                LoanPurpose.PERSONAL
        );
    }

    private void logCheck(String check, Object expected, Object actual) {
        log.info("{}: expected={}, actual={}", check, expected, actual);
    }
}
