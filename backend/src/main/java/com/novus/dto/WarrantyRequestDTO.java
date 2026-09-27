package com.novus.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class WarrantyRequestDTO {

    @NotNull(message = "Warranty months is required")
    @Positive(message = "Warranty months must be greater than 0")
    private Integer warrantyMonths;

    private LocalDate startsAt; // Optional override

    public Integer getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(Integer warrantyMonths) {
        this.warrantyMonths = warrantyMonths;
    }

    public LocalDate getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDate startsAt) {
        this.startsAt = startsAt;
    }
}
