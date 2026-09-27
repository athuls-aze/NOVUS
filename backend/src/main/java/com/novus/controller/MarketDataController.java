package com.novus.controller;

import com.novus.marketdata.MarketDataPoint;
import com.novus.marketdata.MarketDataProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/market-data")
public class MarketDataController {

    private final MarketDataProvider marketDataProvider;

    public MarketDataController(MarketDataProvider marketDataProvider) {
        this.marketDataProvider = marketDataProvider;
    }

    @GetMapping("/{variantId}")
    public List<MarketDataPoint> getTrendData(@PathVariable Long variantId) {
        return marketDataProvider.getTrendData(variantId);
    }
}
