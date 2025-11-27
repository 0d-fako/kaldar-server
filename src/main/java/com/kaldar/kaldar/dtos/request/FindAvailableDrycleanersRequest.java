package com.kaldar.kaldar.dtos.request;

import jakarta.validation.constraints.*;
import java.util.List;

public class FindAvailableDrycleanersRequest {

    @NotNull(message = "lat is required")
    @DecimalMin(value = "-90.0", message = "lat must be >= -90")
    @DecimalMax(value = "90.0", message = "lat must be <= 90")
    private Double lat;

    @NotNull(message = "lng is required")
    @DecimalMin(value = "-180.0", message = "lng must be >= -180")
    @DecimalMax(value = "180.0", message = "lng must be <= 180")
    private Double lng;

    @NotNull(message = "radiusKm is required")
    @DecimalMin(value = "0.1", inclusive = true, message = "radiusKm must be >= 0.1")
    @DecimalMax(value = "50.0", inclusive = true, message = "radiusKm must be <= 50")
    private Double radiusKm = 5.0;

    private List<String> services;

    private Boolean openNow;

    // sort by distance|rating|price (future-proof). Default distance
    private String sort = "distance";

    @Min(value = 0, message = "page must be >= 0")
    private Integer page = 0;

    @Min(value = 1, message = "size must be >= 1")
    @Max(value = 100, message = "size must be <= 100")
    private Integer size = 20;

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }

    public Double getRadiusKm() { return radiusKm; }
    public void setRadiusKm(Double radiusKm) { this.radiusKm = radiusKm; }

    public List<String> getServices() { return services; }
    public void setServices(List<String> services) { this.services = services; }

    public Boolean getOpenNow() { return openNow; }
    public void setOpenNow(Boolean openNow) { this.openNow = openNow; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }

    public Integer getPage() { return page; }
    public void setPage(Integer page) { this.page = page; }

    public Integer getSize() { return size; }
    public void setSize(Integer size) { this.size = size; }
}
