package com.kaldar.kaldar.domain.repository;

import com.kaldar.kaldar.domain.entities.OrderServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderServiceItemRepository extends JpaRepository<OrderServiceItem, Long> {
}
