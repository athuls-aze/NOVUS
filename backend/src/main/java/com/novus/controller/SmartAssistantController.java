package com.novus.controller;

import com.novus.dto.SmartAssistantRequestDTO;
import com.novus.dto.SmartAssistantResponseDTO;
import com.novus.service.SmartAssistantService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user-devices/{deviceId}/assistant")
public class SmartAssistantController {

    private final SmartAssistantService smartAssistantService;

    public SmartAssistantController(SmartAssistantService smartAssistantService) {
        this.smartAssistantService = smartAssistantService;
    }

    @PostMapping("/query")
    public SmartAssistantResponseDTO queryAssistant(
            @PathVariable Long deviceId,
            @RequestBody SmartAssistantRequestDTO request,
            Principal principal) {
        return smartAssistantService.processQuery(deviceId, principal.getName(), request);
    }
}
