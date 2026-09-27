package com.novus.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ConditionAssessmentResponseDTO {
    private int overallScore;
    private String status; // EXCELLENT 80-100, GOOD 60-79, FAIR 40-59, POOR 0-39
    private Integer batteryHealth;
    private String displayCondition;
    private String bodyCondition;
    private String cameraCondition;
    private String chargingCondition;
    private List<ConditionScoreBreakdownDTO> scoreBreakdown;
    private LocalDateTime assessedAt;

    public int getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(int overallScore) {
        this.overallScore = overallScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getBatteryHealth() {
        return batteryHealth;
    }

    public void setBatteryHealth(Integer batteryHealth) {
        this.batteryHealth = batteryHealth;
    }

    public String getDisplayCondition() {
        return displayCondition;
    }

    public void setDisplayCondition(String displayCondition) {
        this.displayCondition = displayCondition;
    }

    public String getBodyCondition() {
        return bodyCondition;
    }

    public void setBodyCondition(String bodyCondition) {
        this.bodyCondition = bodyCondition;
    }

    public String getCameraCondition() {
        return cameraCondition;
    }

    public void setCameraCondition(String cameraCondition) {
        this.cameraCondition = cameraCondition;
    }

    public String getChargingCondition() {
        return chargingCondition;
    }

    public void setChargingCondition(String chargingCondition) {
        this.chargingCondition = chargingCondition;
    }

    public List<ConditionScoreBreakdownDTO> getScoreBreakdown() {
        return scoreBreakdown;
    }

    public void setScoreBreakdown(List<ConditionScoreBreakdownDTO> scoreBreakdown) {
        this.scoreBreakdown = scoreBreakdown;
    }

    public LocalDateTime getAssessedAt() {
        return assessedAt;
    }

    public void setAssessedAt(LocalDateTime assessedAt) {
        this.assessedAt = assessedAt;
    }
}
