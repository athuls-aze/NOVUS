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
@Table(name = "sale_requests")
public class SaleRequest extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_device_id", nullable = false)
    private UserDevice userDevice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_offer_id")
    private Offer selectedOffer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50, columnDefinition = "VARCHAR(50)")
    private SaleStatus status;

    public UserDevice getUserDevice() { return userDevice; }
    public void setUserDevice(UserDevice userDevice) { this.userDevice = userDevice; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Offer getSelectedOffer() { return selectedOffer; }
    public void setSelectedOffer(Offer selectedOffer) { this.selectedOffer = selectedOffer; }
    public SaleStatus getStatus() { return status; }
    public void setStatus(SaleStatus status) { this.status = status; }
}
