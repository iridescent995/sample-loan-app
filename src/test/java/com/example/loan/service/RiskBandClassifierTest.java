package com.example.loan.service;

import com.example.loan.domain.RiskBand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RiskBandClassifierTest {

    private static final Logger log = LoggerFactory.getLogger(RiskBandClassifierTest.class);

    private final RiskBandClassifier classifier = new RiskBandClassifier();

    @Test
    void classifiesBoundaryScores() {
        logCheck("Risk band for score 750", RiskBand.LOW, classifier.classify(750));
        logCheck("Risk band for score 749", RiskBand.MEDIUM, classifier.classify(749));
        logCheck("Risk band for score 650", RiskBand.MEDIUM, classifier.classify(650));
        logCheck("Risk band for score 649", RiskBand.HIGH, classifier.classify(649));
        logCheck("Risk band for score 600", RiskBand.HIGH, classifier.classify(600));

        assertEquals(RiskBand.LOW, classifier.classify(750));
        assertEquals(RiskBand.MEDIUM, classifier.classify(749));
        assertEquals(RiskBand.MEDIUM, classifier.classify(650));
        assertEquals(RiskBand.HIGH, classifier.classify(649));
        assertEquals(RiskBand.HIGH, classifier.classify(600));
    }

    @Test
    void rejectsScoresBelowEligibleRange() {
        log.info("Risk band for score 599: expected IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> classifier.classify(599));
    }

    private void logCheck(String check, Object expected, Object actual) {
        log.info("{}: expected={}, actual={}", check, expected, actual);
    }
}
