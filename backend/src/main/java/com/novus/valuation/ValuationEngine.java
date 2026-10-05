package com.novus.valuation;

import com.novus.marketdata.MarketDataProvider;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class ValuationEngine {

    private final MarketDataProvider marketDataProvider;

    public ValuationEngine(MarketDataProvider marketDataProvider) {
        this.marketDataProvider = marketDataProvider;
    }

    public ValuationResult calculate(BigDecimal basePrice, int conditionScore, LocalDate purchaseDate, Long variantId) {
        // 1. ageMonths
        long ageMonthsLong = 12; // default
        if (purchaseDate != null) {
            ageMonthsLong = ChronoUnit.MONTHS.between(purchaseDate, LocalDate.now());
            if (ageMonthsLong < 0) ageMonthsLong = 0;
        }
        int ageMonths = (int) ageMonthsLong;

        // 2. ageDepreciationRate = min(ageMonths * 0.8, 60.0)
        double ageDepreciationRate = Math.min(ageMonths * 0.8, 60.0);

        // 3. conditionPenalty = (100 - conditionScore) * 0.3
        double conditionPenalty = (100 - conditionScore) * 0.3;

        // 4. trendMultiplier
        double trendMultiplier = marketDataProvider.getCurrentTrendMultiplier(variantId);

        // 5. adjustedDepreciation = ageDepreciationRate + conditionPenalty
        double adjustedDepreciation = ageDepreciationRate + conditionPenalty;

        // 6. estimatedValue = basePrice * (1 - adjustedDepreciation/100.0) * trendMultiplier
        // Clamp to 0 so extremely old/poor-condition devices don't produce negative values
        double basePriceDouble = basePrice.doubleValue();
        double estimatedValueDouble = basePriceDouble * Math.max(0.0, (1 - adjustedDepreciation / 100.0)) * trendMultiplier;
        BigDecimal estimatedValue = BigDecimal.valueOf(estimatedValueDouble).setScale(2, RoundingMode.HALF_UP);

        // 7. lowerRange = estimatedValue * 0.90
        BigDecimal lowerRange = estimatedValue.multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP);

        // 8. upperRange = estimatedValue * 1.10
        BigDecimal upperRange = estimatedValue.multiply(new BigDecimal("1.10")).setScale(2, RoundingMode.HALF_UP);

        // 9. confidenceScore
        int confidenceScore = 80;
        if (conditionScore > 70) confidenceScore += 10;
        if (ageMonths > 24) confidenceScore -= 10;
        if (trendMultiplier < 1.0) confidenceScore -= 10;
        
        confidenceScore = Math.max(30, Math.min(confidenceScore, 95));

        // 10. explanation
        StringBuilder explanation = new StringBuilder();
        explanation.append("Base Price: ").append(basePrice).append(". ");
        explanation.append("Age (months): ").append(ageMonths).append(" (").append(String.format("%.1f", ageDepreciationRate)).append("% depreciation). ");
        explanation.append("Condition Score: ").append(conditionScore).append(" (").append(String.format("%.1f", conditionPenalty)).append("% penalty). ");
        explanation.append("Market Trend: ").append(trendMultiplier).append("x multiplier.");

        ValuationResult result = new ValuationResult();
        result.setEstimatedValue(estimatedValue);
        result.setLowerRange(lowerRange);
        result.setUpperRange(upperRange);
        result.setConfidenceScore(confidenceScore);
        result.setAgeMonths(ageMonths);
        result.setTrendMultiplier(trendMultiplier);
        result.setConditionScore(conditionScore);
        result.setExplanation(explanation.toString());

        return result;
    }
}
