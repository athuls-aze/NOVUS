package com.novus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "market_data")
public class MarketData extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private Variant variant;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "average_market_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal averageMarketPrice;

    @Column(name = "trend_indicator", nullable = false, length = 20)
    private String trendIndicator;

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
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
