package com.kaldar.kaldar.dtos.response;

import java.time.LocalDateTime;
import java.util.List;

public class CreateOrderResponse {
    private Long orderId;
    private Long customerId;
    private Long dryCleanerId;
    private String pickupAddress;
    private String deliveryAddress;
    private double totalPrice;
    private String status;
    private LocalDateTime createdAt;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getDryCleanerId() {
        return dryCleanerId;
    }

    public void setDryCleanerId(Long dryCleanerId) {
        this.dryCleanerId = dryCleanerId;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
