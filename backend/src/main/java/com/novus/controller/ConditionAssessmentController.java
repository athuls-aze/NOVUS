package com.novus.controller;

import com.novus.dto.ConditionAssessmentRequestDTO;
import com.novus.dto.ConditionAssessmentResponseDTO;
import com.novus.service.ConditionAssessmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user-devices/{deviceId}/condition")
public class ConditionAssessmentController {

    private final ConditionAssessmentService assessmentService;

    public ConditionAssessmentController(ConditionAssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConditionAssessmentResponseDTO submitAssessment(
            @PathVariable Long deviceId,
            @Valid @RequestBody ConditionAssessmentRequestDTO request,
            Principal principal) {
        return assessmentService.submitAssessment(deviceId, principal.getName(), request);
    }

    @GetMapping
    public ConditionAssessmentResponseDTO getLatestAssessment(
            @PathVariable Long deviceId,
            Principal principal) {
        return assessmentService.getLatestAssessment(deviceId, principal.getName());
    }
}
