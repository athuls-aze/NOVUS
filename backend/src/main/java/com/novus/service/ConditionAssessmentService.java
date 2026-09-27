package com.novus.service;

import com.novus.dto.ConditionAssessmentRequestDTO;
import com.novus.dto.ConditionAssessmentResponseDTO;
import com.novus.entity.ConditionAssessment;
import com.novus.entity.ConditionGrade;
import com.novus.entity.User;
import com.novus.entity.UserDevice;
import com.novus.repository.ConditionAssessmentRepository;
import com.novus.repository.UserDeviceRepository;
import com.novus.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ConditionAssessmentService {

    private final ConditionAssessmentRepository assessmentRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final UserRepository userRepository;
    private final ConditionScoringService scoringService;

    public ConditionAssessmentService(ConditionAssessmentRepository assessmentRepository,
                                      UserDeviceRepository userDeviceRepository,
                                      UserRepository userRepository,
                                      ConditionScoringService scoringService) {
        this.assessmentRepository = assessmentRepository;
        this.userDeviceRepository = userDeviceRepository;
        this.userRepository = userRepository;
        this.scoringService = scoringService;
    }

    public ConditionAssessmentResponseDTO submitAssessment(Long deviceId, String userEmail, ConditionAssessmentRequestDTO request) {
        UserDevice userDevice = validateAndGetDevice(deviceId, userEmail);

        ConditionGrade display = parseGrade(request.getDisplayCondition());
        ConditionGrade body = parseGrade(request.getBodyCondition());
        ConditionGrade camera = parseGrade(request.getCameraCondition());
        ConditionGrade charging = parseGrade(request.getChargingCondition());

        int overallScore = scoringService.calculateOverallScore(request.getBatteryHealth(), display, body, camera, charging);

        ConditionAssessment assessment = new ConditionAssessment();
        assessment.setUserDevice(userDevice);
        assessment.setBatteryHealth(request.getBatteryHealth());
        assessment.setDisplayCondition(display);
        assessment.setBodyCondition(body);
        assessment.setCameraCondition(camera);
        assessment.setChargingCondition(charging);
        assessment.setOverallScore(overallScore);

        ConditionAssessment saved = assessmentRepository.save(assessment);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public ConditionAssessmentResponseDTO getLatestAssessment(Long deviceId, String userEmail) {
        validateAndGetDevice(deviceId, userEmail);

        ConditionAssessment assessment = assessmentRepository.findFirstByUserDeviceIdOrderByCreatedAtDesc(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No condition assessment found for this device"));

        return mapToResponse(assessment);
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

    private ConditionGrade parseGrade(String gradeStr) {
        try {
            return ConditionGrade.valueOf(gradeStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid condition grade: " + gradeStr);
        }
    }

    private ConditionAssessmentResponseDTO mapToResponse(ConditionAssessment assessment) {
        ConditionAssessmentResponseDTO response = new ConditionAssessmentResponseDTO();
        response.setOverallScore(assessment.getOverallScore());
        response.setStatus(determineStatus(assessment.getOverallScore()));
        response.setBatteryHealth(assessment.getBatteryHealth());
        response.setDisplayCondition(assessment.getDisplayCondition().name());
        response.setBodyCondition(assessment.getBodyCondition().name());
        response.setCameraCondition(assessment.getCameraCondition().name());
        response.setChargingCondition(assessment.getChargingCondition().name());
        
        response.setScoreBreakdown(scoringService.getBreakdown(
                assessment.getBatteryHealth(),
                assessment.getDisplayCondition(),
                assessment.getBodyCondition(),
                assessment.getCameraCondition(),
                assessment.getChargingCondition()
        ));
        
        response.setAssessedAt(assessment.getCreatedAt());
        
        return response;
    }

    private String determineStatus(int score) {
        if (score >= 80) return "EXCELLENT";
        if (score >= 60) return "GOOD";
        if (score >= 40) return "FAIR";
        return "POOR";
    }
}
