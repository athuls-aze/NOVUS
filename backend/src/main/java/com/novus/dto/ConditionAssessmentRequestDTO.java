package com.novus.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConditionAssessmentRequestDTO {

    @NotNull(message = "Battery health is required")
    @Min(value = 0, message = "Battery health cannot be less than 0")
    @Max(value = 100, message = "Battery health cannot be greater than 100")
    private Integer batteryHealth;

    @NotBlank(message = "Display condition is required")
    private String displayCondition;

    @NotBlank(message = "Body condition is required")
    private String bodyCondition;

    @NotBlank(message = "Camera condition is required")
    private String cameraCondition;

    @NotBlank(message = "Charging condition is required")
    private String chargingCondition;

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
}
