package com.novus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "valuations")
public class Valuation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_device_id", nullable = false)
    private UserDevice userDevice;

    @Column(name = "estimated_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedValue;

    @Column(name = "lower_range", nullable = false, precision = 10, scale = 2)
    private BigDecimal lowerRange;

    @Column(name = "upper_range", nullable = false, precision = 10, scale = 2)
    private BigDecimal upperRange;

    @Column(name = "confidence_score", nullable = false)
    private Integer confidenceScore;

    @Column(name = "condition_score", nullable = false)
    private Integer conditionScore;

    @Column(name = "age_months", nullable = false)
    private Integer ageMonths;

    @Column(name = "trend_multiplier", nullable = false, precision = 5, scale = 4)
    private BigDecimal trendMultiplier;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name = "calculated_at", nullable = false)
    private LocalDateTime calculatedAt;

    public UserDevice getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
    }

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

    public Integer getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Integer confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public Integer getConditionScore() {
        return conditionScore;
    }

    public void setConditionScore(Integer conditionScore) {
        this.conditionScore = conditionScore;
    }

    public Integer getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(Integer ageMonths) {
        this.ageMonths = ageMonths;
    }

    public BigDecimal getTrendMultiplier() {
        return trendMultiplier;
    }

    public void setTrendMultiplier(BigDecimal trendMultiplier) {
        this.trendMultiplier = trendMultiplier;
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
