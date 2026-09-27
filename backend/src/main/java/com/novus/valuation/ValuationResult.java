package com.novus.valuation;

import java.math.BigDecimal;

public class ValuationResult {
    private BigDecimal estimatedValue;
    private BigDecimal lowerRange;
    private BigDecimal upperRange;
    private int confidenceScore;
    private int ageMonths;
    private double trendMultiplier;
    private int conditionScore;
    private String explanation;

    public BigDecimal getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(BigDecimal estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public BigDecimal getLowerRange() {
        return lowerRange;
    }

    public void setLowerRange(BigDecimal lowerRange) {
        this.lowerRange = lowerRange;
    }

    public BigDecimal getUpperRange() {
        return upperRange;
    }

    public void setUpperRange(BigDecimal upperRange) {
        this.upperRange = upperRange;
    }

    public int getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(int confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public int getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    public double getTrendMultiplier() {
        return trendMultiplier;
    }

    public void setTrendMultiplier(double trendMultiplier) {
        this.trendMultiplier = trendMultiplier;
    }

    public int getConditionScore() {
        return conditionScore;
    }

    public void setConditionScore(int conditionScore) {
        this.conditionScore = conditionScore;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
