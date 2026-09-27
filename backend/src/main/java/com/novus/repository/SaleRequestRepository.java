package com.novus.repository;

import com.novus.entity.SaleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaleRequestRepository extends JpaRepository<SaleRequest, Long> {
    Optional<SaleRequest> findByUserDeviceId(Long deviceId);
}
