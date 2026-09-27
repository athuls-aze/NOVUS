package com.novus.marketdata;

import java.time.LocalDate;
import java.math.BigDecimal;

public class MarketDataPoint {
    private Long variantId;
    private LocalDate recordDate;
    private BigDecimal averageMarketPrice;
    private String trendIndicator; // RISING, STABLE, FALLING

    public Long getVariantId() {
        return variantId;
    }

    public void setVariantId(Long variantId) {
        this.variantId = variantId;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public BigDecimal getAverageMarketPrice() {
        return averageMarketPrice;
    }

    public void setAverageMarketPrice(BigDecimal averageMarketPrice) {
        this.averageMarketPrice = averageMarketPrice;
    }

    public String getTrendIndicator() {
        return trendIndicator;
    }

    public void setTrendIndicator(String trendIndicator) {
        this.trendIndicator = trendIndicator;
    }
}
