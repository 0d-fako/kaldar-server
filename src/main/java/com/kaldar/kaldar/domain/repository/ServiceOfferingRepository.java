package com.kaldar.kaldar.domain.repository;

import com.kaldar.kaldar.domain.entities.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
//    ServiceOffering findByDryCleanerIdAndClothesType(Long dryCleanerId, String clothType);


}
