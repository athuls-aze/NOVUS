package com.novus.controller;

import com.novus.dto.ValuationResponseDTO;
import com.novus.service.ValuationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user-devices/{deviceId}/valuation")
public class ValuationController {

    private final ValuationService valuationService;

    public ValuationController(ValuationService valuationService) {
        this.valuationService = valuationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ValuationResponseDTO calculateAndSave(
            @PathVariable Long deviceId,
            Principal principal) {
        return valuationService.calculateAndSave(deviceId, principal.getName());
    }

    @GetMapping
    public ValuationResponseDTO getLatestValuation(
            @PathVariable Long deviceId,
            Principal principal) {
        return valuationService.getLatestValuation(deviceId, principal.getName());
    }
}
