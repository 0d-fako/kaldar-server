package com.kaldar.kaldar.domain.repository;

import com.kaldar.kaldar.domain.entities.DryCleanerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DryCleanerEntityRepository extends JpaRepository<DryCleanerEntity, Long> {

    boolean existsByEmail(String email);

    Page<DryCleanerEntity> findByIsActiveTrueAndVerifiedUserTrue(Pageable pageable);
}
