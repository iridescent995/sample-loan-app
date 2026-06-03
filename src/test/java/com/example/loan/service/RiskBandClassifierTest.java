package com.example.loan.service;

import com.example.loan.domain.RiskBand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RiskBandClassifierTest {

    private final RiskBandClassifier classifier = new RiskBandClassifier();

    @Test
    void classifiesBoundaryScores() {
        printCheck("Risk band for score 750", RiskBand.LOW, classifier.classify(750));
        printCheck("Risk band for score 749", RiskBand.MEDIUM, classifier.classify(749));
        printCheck("Risk band for score 650", RiskBand.MEDIUM, classifier.classify(650));
        printCheck("Risk band for score 649", RiskBand.HIGH, classifier.classify(649));
        printCheck("Risk band for score 600", RiskBand.HIGH, classifier.classify(600));

        assertEquals(RiskBand.LOW, classifier.classify(750));
        assertEquals(RiskBand.MEDIUM, classifier.classify(749));
        assertEquals(RiskBand.MEDIUM, classifier.classify(650));
        assertEquals(RiskBand.HIGH, classifier.classify(649));
        assertEquals(RiskBand.HIGH, classifier.classify(600));
    }

    @Test
    void rejectsScoresBelowEligibleRange() {
        System.out.println("Risk band for score 599: expected IllegalArgumentException");

        assertThrows(IllegalArgumentException.class, () -> classifier.classify(599));
    }

    private void printCheck(String check, Object expected, Object actual) {
        System.out.printf("%s: expected=%s, actual=%s%n", check, expected, actual);
    }
}
