package com.kaldar.kaldar.dtos.request;
import com.kaldar.kaldar.domain.entities.OrderServiceItem;

import java.time.LocalDateTime;
import java.util.List;

public class CreateOrderRequest {
    private Long customerId;
    private Long dryCleanerId;

    private String pickupAddress;
    private String deliveryAddress;

    private String washingPreference;
    private LocalDateTime createdAt;
    private List<OrderServiceItem> serviceItems;

    public Long getDryCleanerId() {
        return dryCleanerId;
    }

    public void setDryCleanerId(Long dryCleanerId) {
        this.dryCleanerId = dryCleanerId;
    }

    public String getWashingPreference() {
        return washingPreference;
    }

    public void setWashingPreference(String washingPreference) {
        this.washingPreference = washingPreference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<OrderServiceItem> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<OrderServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }
}
