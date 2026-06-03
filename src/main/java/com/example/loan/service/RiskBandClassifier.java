package com.example.loan.service;

import com.example.loan.domain.RiskBand;
import org.springframework.stereotype.Service;

@Service
public class RiskBandClassifier {

    public RiskBand classify(int creditScore) {
        if (creditScore >= 750) {
            return RiskBand.LOW;
        }
        if (creditScore >= 650) {
            return RiskBand.MEDIUM;
        }
        if (creditScore >= 600) {
            return RiskBand.HIGH;
        }
        throw new IllegalArgumentException("creditScore must be at least 600 to classify risk");
    }
}
