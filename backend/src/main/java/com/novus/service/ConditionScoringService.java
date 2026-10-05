package com.novus.service;

import com.novus.dto.ConditionScoreBreakdownDTO;
import com.novus.entity.ConditionGrade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConditionScoringService {

    public int calculateOverallScore(int batteryHealth, ConditionGrade display, ConditionGrade body, ConditionGrade camera, ConditionGrade charging) {
        double total = (batteryHealth * 0.30) + 
                       (getGradeScore(display) * 0.25) + 
                       (getGradeScore(body) * 0.20) + 
                       (getGradeScore(camera) * 0.15) + 
                       (getGradeScore(charging) * 0.10);
        return (int) Math.round(total);
    }

    public List<ConditionScoreBreakdownDTO> getBreakdown(int batteryHealth, ConditionGrade display, ConditionGrade body, ConditionGrade camera, ConditionGrade charging) {
        List<ConditionScoreBreakdownDTO> breakdown = new ArrayList<>();
        breakdown.add(new ConditionScoreBreakdownDTO("Battery Health", 0.30, batteryHealth, batteryHealth * 0.30));
        breakdown.add(new ConditionScoreBreakdownDTO("Display Condition", 0.25, getGradeScore(display), getGradeScore(display) * 0.25));
        breakdown.add(new ConditionScoreBreakdownDTO("Body Condition", 0.20, getGradeScore(body), getGradeScore(body) * 0.20));
        breakdown.add(new ConditionScoreBreakdownDTO("Camera Condition", 0.15, getGradeScore(camera), getGradeScore(camera) * 0.15));
        breakdown.add(new ConditionScoreBreakdownDTO("Charging Condition", 0.10, getGradeScore(charging), getGradeScore(charging) * 0.10));
        return breakdown;
    }

    private int getGradeScore(ConditionGrade grade) {
        return switch (grade) {
            case EXCELLENT -> 100;
            case GOOD -> 75;
            case FAIR -> 45;
            case POOR -> 15;
        };
    }
}
