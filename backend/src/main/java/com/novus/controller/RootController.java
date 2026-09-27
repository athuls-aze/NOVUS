package com.novus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> rootWelcome() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("service", "NOVUS Smartphone Valuation & Lifecycle Platform");
        response.put("version", "1.0.0");
        response.put("message", "NOVUS Backend API is actively running.");
        response.put("frontendUrl", "http://localhost:5173");
        response.put("apiPrefix", "/api/v1");
        response.put("healthCheck", "/api/v1/health");
        response.put("catalog", "/api/v1/catalog/brands");
        return ResponseEntity.ok(response);
    }
}
