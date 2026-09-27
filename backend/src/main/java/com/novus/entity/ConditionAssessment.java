package com.novus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "condition_assessments")
public class ConditionAssessment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_device_id", nullable = false)
    private UserDevice userDevice;

    @Column(name = "battery_health", nullable = false)
    private Integer batteryHealth;

    @Enumerated(EnumType.STRING)
    @Column(name = "display_condition", nullable = false, length = 10, columnDefinition = "VARCHAR(10)")
    private ConditionGrade displayCondition;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_condition", nullable = false, length = 10, columnDefinition = "VARCHAR(10)")
    private ConditionGrade bodyCondition;

    @Enumerated(EnumType.STRING)
    @Column(name = "camera_condition", nullable = false, length = 10, columnDefinition = "VARCHAR(10)")
    private ConditionGrade cameraCondition;

    @Enumerated(EnumType.STRING)
    @Column(name = "charging_condition", nullable = false, length = 10, columnDefinition = "VARCHAR(10)")
    private ConditionGrade chargingCondition;

    @Column(name = "overall_score", nullable = false)
    private Integer overallScore;

    public UserDevice getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
    }

    public Integer getBatteryHealth() {
        return batteryHealth;
    }

    public void setBatteryHealth(Integer batteryHealth) {
        this.batteryHealth = batteryHealth;
    }

    public ConditionGrade getDisplayCondition() {
        return displayCondition;
    }

    public void setDisplayCondition(ConditionGrade displayCondition) {
        this.displayCondition = displayCondition;
    }

    public ConditionGrade getBodyCondition() {
        return bodyCondition;
    }

    public void setBodyCondition(ConditionGrade bodyCondition) {
        this.bodyCondition = bodyCondition;
    }

    public ConditionGrade getCameraCondition() {
        return cameraCondition;
    }

    public void setCameraCondition(ConditionGrade cameraCondition) {
        this.cameraCondition = cameraCondition;
    }

    public ConditionGrade getChargingCondition() {
        return chargingCondition;
    }

    public void setChargingCondition(ConditionGrade chargingCondition) {
        this.chargingCondition = chargingCondition;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }
}
