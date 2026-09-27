package com.novus.valuation;

import com.novus.marketdata.MarketDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class ValuationEngineTest {

    private MarketDataProvider marketDataProvider;
    private ValuationEngine engine;

    @BeforeEach
    void setUp() {
        marketDataProvider = Mockito.mock(MarketDataProvider.class);
        engine = new ValuationEngine(marketDataProvider);
    }

    @Test
    void testCalculateValuation() {
        Long variantId = 1L;
        BigDecimal basePrice = new BigDecimal("50000");
        int conditionScore = 80;
        LocalDate purchaseDate = LocalDate.now().minusMonths(12);

        when(marketDataProvider.getCurrentTrendMultiplier(variantId)).thenReturn(1.0);

        ValuationResult result = engine.calculate(basePrice, conditionScore, purchaseDate, variantId);

        // Age: 12 months -> 12 * 0.8 = 9.6%
        // Condition Penalty: (100 - 80) * 0.3 = 6.0%
        // Total depreciation = 15.6%
        // Estimated Value = 50000 * (1 - 0.156) = 42200

        assertEquals(new BigDecimal("42200.00"), result.getEstimatedValue());
        assertEquals(12, result.getAgeMonths());
        assertTrue(result.getConfidenceScore() >= 80);
    }
}
