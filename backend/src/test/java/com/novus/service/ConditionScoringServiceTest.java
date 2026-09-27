package com.novus.service;

import com.novus.entity.ConditionGrade;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionScoringServiceTest {

    @Test
    void testCalculateOverallScore() {
        ConditionScoringService service = new ConditionScoringService();
        
        // EXCELLENT=100, GOOD=75, FAIR=45, POOR=15
        // Battery: 90 * 0.30 = 27
        // Display: GOOD (75) * 0.25 = 18.75
        // Body: FAIR (45) * 0.20 = 9.0
        // Camera: EXCELLENT (100) * 0.15 = 15.0
        // Charging: POOR (15) * 0.10 = 1.5
        // Total = 27 + 18.75 + 9.0 + 15.0 + 1.5 = 71.25 -> 71

        int score = service.calculateOverallScore(90, ConditionGrade.GOOD, ConditionGrade.FAIR, ConditionGrade.EXCELLENT, ConditionGrade.POOR);
        
        assertEquals(71, score);
    }
}
