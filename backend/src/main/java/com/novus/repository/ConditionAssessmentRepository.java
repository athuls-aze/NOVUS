package com.novus.repository;

import com.novus.entity.ConditionAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConditionAssessmentRepository extends JpaRepository<ConditionAssessment, Long> {
    List<ConditionAssessment> findByUserDeviceIdOrderByCreatedAtDesc(Long deviceId);
    Optional<ConditionAssessment> findFirstByUserDeviceIdOrderByCreatedAtDesc(Long deviceId);
}
