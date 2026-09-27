package com.novus.dto;

import java.time.LocalDate;

public class ModelDTO {
    private Long id;
    private String name;
    private LocalDate releaseDate;
    private Long seriesId;

    public ModelDTO(Long id, String name, LocalDate releaseDate, Long seriesId) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.seriesId = seriesId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public Long getSeriesId() { return seriesId; }
    public void setSeriesId(Long seriesId) { this.seriesId = seriesId; }
}
