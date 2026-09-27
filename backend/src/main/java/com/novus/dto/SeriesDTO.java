package com.novus.dto;

public class SeriesDTO {
    private Long id;
    private String name;
    private Long brandId;

    public SeriesDTO(Long id, String name, Long brandId) {
        this.id = id;
        this.name = name;
        this.brandId = brandId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
}
