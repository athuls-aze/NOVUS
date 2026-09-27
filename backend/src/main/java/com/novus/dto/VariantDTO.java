package com.novus.dto;

import java.math.BigDecimal;

public class VariantDTO {
    private Long id;
    private String storage;
    private String ram;
    private String color;
    private BigDecimal basePrice;
    private Long modelId;

    public VariantDTO(Long id, String storage, String ram, String color, BigDecimal basePrice, Long modelId) {
        this.id = id;
        this.storage = storage;
        this.ram = ram;
        this.color = color;
        this.basePrice = basePrice;
        this.modelId = modelId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStorage() { return storage; }
    public void setStorage(String storage) { this.storage = storage; }
    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }
    public Long getModelId() { return modelId; }
    public void setModelId(Long modelId) { this.modelId = modelId; }
}
