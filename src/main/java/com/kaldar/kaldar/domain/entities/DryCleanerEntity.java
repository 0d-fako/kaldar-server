package com.kaldar.kaldar.domain.entities;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;



@Entity
public class DryCleanerEntity extends UserEntity{

    @NotBlank(message = "Business name is required")
    private String businessName;
    @NotBlank(message = "Business address is required")
    private String businessAddress;
    private String workingHours;
    @OneToMany(mappedBy = "dryCleaner", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderEntity> orderEntityList;
    @OneToMany(mappedBy = "dryCleaner")
    private List<ServiceOffering> serviceOfferings;
    private boolean isActive;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



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

    public @NotBlank(message = "Business name is required") String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(@NotBlank(message = "Business name is required") String businessName) {
        this.businessName = businessName;
    }

    public @NotBlank(message = "Business address is required") String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(@NotBlank(message = "Business address is required") String businessAddress) {
        this.businessAddress = businessAddress;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public List<OrderEntity> getOrderEntityList() {
        return orderEntityList;
    }

    public void setOrderEntityList(List<OrderEntity> orderEntityList) {
        this.orderEntityList = orderEntityList;
    }

    public List<ServiceOffering> getServiceOfferings() {
        return serviceOfferings;
    }

    public void setServiceOfferings(List<ServiceOffering> serviceOfferings) {
        this.serviceOfferings = serviceOfferings;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
