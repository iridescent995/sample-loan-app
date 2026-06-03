package com.example.loan.service;

import com.example.loan.domain.RiskBand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RiskBandClassifier {

    private static final Logger log = LoggerFactory.getLogger(RiskBandClassifier.class);

    public RiskBand classify(int creditScore) {
        if (creditScore >= 750) {
            return logAndReturn(RiskBand.LOW);
        }
        if (creditScore >= 650) {
            return logAndReturn(RiskBand.MEDIUM);
        }
        if (creditScore >= 600) {
            return logAndReturn(RiskBand.HIGH);
        }
        throw new IllegalArgumentException("creditScore must be at least 600 to classify risk");
    }

    private RiskBand logAndReturn(RiskBand riskBand) {
        log.debug("Classified the applicant into the {} risk band", riskBand);
        return riskBand;
    }
}
