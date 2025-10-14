package com.kaldar.kaldar.domain.entities;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class ServiceOffering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serviceName;
    private String description;
    private Double unitPrice; //price per clothes or items
    @ManyToOne
    @JoinColumn(name = "dry_cleaner_id")
    private DryCleanerEntity dryCleaner;
    private Boolean expressAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer estimatedTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public DryCleanerEntity getDryCleaner() {
        return dryCleaner;
    }

    public void setDryCleaner(DryCleanerEntity dryCleaner) {
        this.dryCleaner = dryCleaner;
    }

    public Boolean getExpressAvailable() {
        return expressAvailable;
    }

    public void setExpressAvailable(Boolean expressAvailable) {
        this.expressAvailable = expressAvailable;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
