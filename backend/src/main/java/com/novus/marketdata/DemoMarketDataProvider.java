package com.novus.marketdata;

import com.novus.entity.MarketData;
import com.novus.repository.MarketDataRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DEMO IMPLEMENTATION — returns curated hardcoded seed data.
 * Swap this implementation for a real provider in production.
 */
@Service
@Primary
public class DemoMarketDataProvider implements MarketDataProvider {

    private final MarketDataRepository marketDataRepository;

    public DemoMarketDataProvider(MarketDataRepository marketDataRepository) {
        this.marketDataRepository = marketDataRepository;
    }

    @Override
    public List<MarketDataPoint> getTrendData(Long variantId) {
        List<MarketData> data = marketDataRepository.findByVariantIdOrderByRecordDateDesc(variantId);
        return data.stream().map(this::mapToPoint).collect(Collectors.toList());
    }

    @Override
    public double getCurrentTrendMultiplier(Long variantId) {
        // Simulating a live market fetch by calculating a dynamic trend multiplier
        // based on the current day of the year to provide "live" daily fluctuations.
        int dayOfYear = java.time.LocalDate.now().getDayOfYear();
        
        // Pseudo-random but consistent for the day
        double fluctuation = Math.sin(dayOfYear + variantId) * 0.08; 
        
        // Base trend is slightly depreciating (-2%) + fluctuation (-8% to +8%)
        double liveTrend = 0.98 + fluctuation;
        
        if (liveTrend > 1.02) {
            return 1.05; // RISING
        } else if (liveTrend < 0.98) {
            return 0.92; // FALLING
        } else {
            return 1.0; // STABLE
        }
    }

    private MarketDataPoint mapToPoint(MarketData data) {
        MarketDataPoint point = new MarketDataPoint();
        point.setVariantId(data.getVariant().getId());
        point.setRecordDate(data.getRecordDate());
        point.setAverageMarketPrice(data.getAverageMarketPrice());
        point.setTrendIndicator(data.getTrendIndicator());
        return point;
    }
}
