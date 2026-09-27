package com.novus.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserDeviceRequestDTO {

    @NotNull(message = "Variant ID is required")
    private Long variantId;

    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate;

    @Positive(message = "Purchase price must be positive")
    private BigDecimal purchasePrice;

    private String storageOverride;
    private String colorOverride;

    // Getters and Setters
    public Long getVariantId() { return variantId; }
    public void setVariantId(Long variantId) { this.variantId = variantId; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    public String getStorageOverride() { return storageOverride; }
    public void setStorageOverride(String storageOverride) { this.storageOverride = storageOverride; }
    public String getColorOverride() { return colorOverride; }
    public void setColorOverride(String colorOverride) { this.colorOverride = colorOverride; }
}
