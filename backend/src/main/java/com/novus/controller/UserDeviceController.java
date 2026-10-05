package com.novus.controller;

import com.novus.dto.UserDeviceRequestDTO;
import com.novus.dto.UserDeviceResponseDTO;
import com.novus.service.UserDeviceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user-devices")
public class UserDeviceController {

    private final UserDeviceService userDeviceService;

    public UserDeviceController(UserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
    }

    @PostMapping
    public ResponseEntity<UserDeviceResponseDTO> registerDevice(@Valid @RequestBody UserDeviceRequestDTO request, Principal principal) {
        UserDeviceResponseDTO response = userDeviceService.registerDevice(request, principal.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserDeviceResponseDTO>> getUserDevices(Principal principal, Pageable pageable) {
        return ResponseEntity.ok(userDeviceService.getUserDevices(principal.getName(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDeviceResponseDTO> getDevice(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(userDeviceService.getDevice(id, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id, Principal principal) {
        userDeviceService.deleteDevice(id, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
