package com.novus.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserDeviceResponseDTO {
    private Long id;
    private Long userId;
    private VariantDTO variant;
    private LocalDate purchaseDate;
    private BigDecimal purchasePrice;
    private String storageOverride;
    private String colorOverride;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public VariantDTO getVariant() { return variant; }
    public void setVariant(VariantDTO variant) { this.variant = variant; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
    public BigDecimal getPurchasePrice() { return purchasePrice; }
    public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    public String getStorageOverride() { return storageOverride; }
    public void setStorageOverride(String storageOverride) { this.storageOverride = storageOverride; }
    public String getColorOverride() { return colorOverride; }
    public void setColorOverride(String colorOverride) { this.colorOverride = colorOverride; }
}
