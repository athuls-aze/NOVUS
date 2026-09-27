package com.novus.marketdata;

import java.util.List;

public interface MarketDataProvider {
    List<MarketDataPoint> getTrendData(Long variantId);
    double getCurrentTrendMultiplier(Long variantId);
}
