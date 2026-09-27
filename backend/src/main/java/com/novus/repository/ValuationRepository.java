package com.novus.repository;

import com.novus.entity.Valuation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValuationRepository extends JpaRepository<Valuation, Long> {
    Optional<Valuation> findFirstByUserDeviceIdOrderByCalculatedAtDesc(Long deviceId);
}
