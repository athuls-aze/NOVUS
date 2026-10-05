package com.novus.service;

import com.novus.dto.BrandDTO;
import com.novus.dto.ModelDTO;
import com.novus.dto.SeriesDTO;
import com.novus.dto.VariantDTO;
import com.novus.repository.BrandRepository;
import com.novus.repository.ModelRepository;
import com.novus.repository.SeriesRepository;
import com.novus.repository.VariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CatalogService {

    private final BrandRepository brandRepository;
    private final SeriesRepository seriesRepository;
    private final ModelRepository modelRepository;
    private final VariantRepository variantRepository;

    public CatalogService(BrandRepository brandRepository, SeriesRepository seriesRepository,
                          ModelRepository modelRepository, VariantRepository variantRepository) {
        this.brandRepository = brandRepository;
        this.seriesRepository = seriesRepository;
        this.modelRepository = modelRepository;
        this.variantRepository = variantRepository;
    }

    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(b -> new BrandDTO(b.getId(), b.getName()))
                .collect(Collectors.toList());
    }

    public List<SeriesDTO> getSeriesByBrand(Long brandId) {
        return seriesRepository.findByBrandId(brandId).stream()
                .map(s -> new SeriesDTO(s.getId(), s.getName(), s.getBrand().getId()))
                .collect(Collectors.toList());
    }

    public List<ModelDTO> getModelsBySeries(Long seriesId) {
        return modelRepository.findBySeriesId(seriesId).stream()
                .map(m -> new ModelDTO(m.getId(), m.getName(), m.getReleaseDate(), m.getSeries().getId()))
                .collect(Collectors.toList());
    }

    public List<VariantDTO> getVariantsByModel(Long modelId) {
        return variantRepository.findByModelId(modelId).stream()
                .map(v -> new VariantDTO(v.getId(), v.getStorage(), v.getRam(), v.getColor(), v.getBasePrice(), v.getModel().getId(), v.getModel().getName()))
                .collect(Collectors.toList());
    }
}
