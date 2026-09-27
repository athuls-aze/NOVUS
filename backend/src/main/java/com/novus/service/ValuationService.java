package com.novus.service;

import com.novus.dto.ValuationResponseDTO;
import com.novus.entity.ConditionAssessment;
import com.novus.entity.User;
import com.novus.entity.UserDevice;
import com.novus.entity.Valuation;
import com.novus.repository.ConditionAssessmentRepository;
import com.novus.repository.UserDeviceRepository;
import com.novus.repository.UserRepository;
import com.novus.repository.ValuationRepository;
import com.novus.valuation.ValuationEngine;
import com.novus.valuation.ValuationResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Transactional
public class ValuationService {

    private final ValuationRepository valuationRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;
    private final ConditionAssessmentRepository conditionAssessmentRepository;
    private final ValuationEngine valuationEngine;

    public ValuationService(ValuationRepository valuationRepository,
                            UserDeviceRepository userDeviceRepository,
                            UserRepository userRepository,
                            ConditionAssessmentRepository conditionAssessmentRepository,
                            ValuationEngine valuationEngine) {
        this.valuationRepository = valuationRepository;
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
        this.conditionAssessmentRepository = conditionAssessmentRepository;
        this.valuationEngine = valuationEngine;
    }

    public ValuationResponseDTO calculateAndSave(Long deviceId, String userEmail) {
        UserDevice userDevice = validateAndGetDevice(deviceId, userEmail);

        ConditionAssessment latestAssessment = conditionAssessmentRepository.findFirstByUserDeviceIdOrderByCreatedAtDesc(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Complete a condition assessment first"));

        ValuationResult result = valuationEngine.calculate(
                userDevice.getVariant().getBasePrice(),
                latestAssessment.getOverallScore(),
                userDevice.getPurchaseDate(),
                userDevice.getVariant().getId()
        );

        Valuation valuation = new Valuation();
        valuation.setUserDevice(userDevice);
        valuation.setEstimatedValue(result.getEstimatedValue());
        valuation.setLowerRange(result.getLowerRange());
        valuation.setUpperRange(result.getUpperRange());
        valuation.setConfidenceScore(result.getConfidenceScore());
        valuation.setConditionScore(result.getConditionScore());
        valuation.setAgeMonths(result.getAgeMonths());
        valuation.setTrendMultiplier(java.math.BigDecimal.valueOf(result.getTrendMultiplier()));
        valuation.setExplanation(result.getExplanation());
        valuation.setCalculatedAt(LocalDateTime.now());

        Valuation saved = valuationRepository.save(valuation);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public ValuationResponseDTO getLatestValuation(Long deviceId, String userEmail) {
        validateAndGetDevice(deviceId, userEmail);

        Valuation valuation = valuationRepository.findFirstByUserDeviceIdOrderByCalculatedAtDesc(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No valuation found for this device"));

        return mapToResponse(valuation);
    }

    private UserDevice validateAndGetDevice(Long deviceId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        UserDevice userDevice = userDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));

        if (!userDevice.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied: Device does not belong to the user");
        }

        return userDevice;
    }

    private ValuationResponseDTO mapToResponse(Valuation valuation) {
        ValuationResponseDTO response = new ValuationResponseDTO();
        response.setEstimatedValue(valuation.getEstimatedValue());
        response.setLowerRange(valuation.getLowerRange());
        response.setUpperRange(valuation.getUpperRange());
        response.setConfidenceScore(valuation.getConfidenceScore());
        response.setAgeMonths(valuation.getAgeMonths());
        response.setTrendMultiplier(valuation.getTrendMultiplier() != null ? valuation.getTrendMultiplier().doubleValue() : 0.0);
        response.setConditionScore(valuation.getConditionScore());
        response.setExplanation(valuation.getExplanation());
        response.setCalculatedAt(valuation.getCalculatedAt());
        return response;
    }
}
