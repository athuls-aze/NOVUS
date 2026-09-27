package com.novus.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ValuationResponseDTO {
    private BigDecimal estimatedValue;
    private BigDecimal lowerRange;
    private BigDecimal upperRange;
    private int confidenceScore;
    private int ageMonths;
    private double trendMultiplier;
    private int conditionScore;
    private String explanation;
    private LocalDateTime calculatedAt;

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

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
}
