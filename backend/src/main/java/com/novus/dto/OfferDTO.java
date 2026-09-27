package com.novus.dto;

import java.math.BigDecimal;

public class OfferDTO {
    private Long id;
    private String shopName;
    private BigDecimal price;
    private String location;
    private BigDecimal distance;
    private String verificationMethod;
    private String paymentMethod;

    public OfferDTO(Long id, String shopName, BigDecimal price, String location, BigDecimal distance, String verificationMethod, String paymentMethod) {
        this.id = id;
        this.shopName = shopName;
        this.price = price;
        this.location = location;
        this.distance = distance;
        this.verificationMethod = verificationMethod;
        this.paymentMethod = paymentMethod;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public BigDecimal getDistance() { return distance; }
    public void setDistance(BigDecimal distance) { this.distance = distance; }
    public String getVerificationMethod() { return verificationMethod; }
    public void setVerificationMethod(String verificationMethod) { this.verificationMethod = verificationMethod; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
