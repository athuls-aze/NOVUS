package com.novus.controller;

import com.novus.dto.SaleRequestDTO;
import com.novus.entity.SaleStatus;
import com.novus.service.SaleRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user-devices/{deviceId}/sale")
public class SaleRequestController {

    private final SaleRequestService saleRequestService;

    public SaleRequestController(SaleRequestService saleRequestService) {
        this.saleRequestService = saleRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleRequestDTO createSaleRequest(@PathVariable Long deviceId, @RequestParam Long offerId, Principal principal) {
        return saleRequestService.createSaleRequest(deviceId, offerId, principal.getName());
    }

    @PutMapping
    public SaleRequestDTO advanceState(@PathVariable Long deviceId, @RequestParam SaleStatus status, Principal principal) {
        return saleRequestService.advanceState(deviceId, status, principal.getName());
    }

    @GetMapping
    public SaleRequestDTO getSaleRequest(@PathVariable Long deviceId, Principal principal) {
        return saleRequestService.getSaleRequest(deviceId, principal.getName());
    }
}
