package com.novus.controller;

import com.novus.dto.WarrantyRequestDTO;
import com.novus.dto.WarrantyResponseDTO;
import com.novus.service.WarrantyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user-devices/{deviceId}/warranty")
public class WarrantyController {

    private final WarrantyService warrantyService;

    public WarrantyController(WarrantyService warrantyService) {
        this.warrantyService = warrantyService;
    }

    @PostMapping
    public ResponseEntity<WarrantyResponseDTO> setWarranty(@PathVariable Long deviceId, 
                                                           @Valid @RequestBody WarrantyRequestDTO request, 
                                                           Principal principal) {
        return new ResponseEntity<>(warrantyService.createOrUpdateWarranty(deviceId, principal.getName(), request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<WarrantyResponseDTO> getWarranty(@PathVariable Long deviceId, Principal principal) {
        return ResponseEntity.ok(warrantyService.getWarranty(deviceId, principal.getName()));
    }
}
