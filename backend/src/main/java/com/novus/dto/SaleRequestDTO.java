package com.novus.dto;

import com.novus.entity.SaleStatus;

public class SaleRequestDTO {
    private Long id;
    private Long deviceId;
    private Long selectedOfferId;
    private SaleStatus status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getSelectedOfferId() { return selectedOfferId; }
    public void setSelectedOfferId(Long selectedOfferId) { this.selectedOfferId = selectedOfferId; }
    public SaleStatus getStatus() { return status; }
    public void setStatus(SaleStatus status) { this.status = status; }
}
