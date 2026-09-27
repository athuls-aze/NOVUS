package com.novus.controller;

import com.novus.dto.BrandDTO;
import com.novus.dto.ModelDTO;
import com.novus.dto.SeriesDTO;
import com.novus.dto.VariantDTO;
import com.novus.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/brands")
    public ResponseEntity<List<BrandDTO>> getBrands() {
        return ResponseEntity.ok(catalogService.getAllBrands());
    }

    @GetMapping("/brands/{id}/series")
    public ResponseEntity<List<SeriesDTO>> getSeriesByBrand(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getSeriesByBrand(id));
    }

    @GetMapping("/series/{id}/models")
    public ResponseEntity<List<ModelDTO>> getModelsBySeries(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getModelsBySeries(id));
    }

    @GetMapping("/models/{id}/variants")
    public ResponseEntity<List<VariantDTO>> getVariantsByModel(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getVariantsByModel(id));
    }
}
