package com.example.agriculture.repository;

import com.example.agriculture.entity.SowingLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SowingLogRepository extends JpaRepository<SowingLogEntity, UUID> {
    List<SowingLogEntity> findByPlantId(UUID plantId);
    List<SowingLogEntity> findBySowingDate(LocalDate date);

    boolean existsById(SowingLogEntity sowingLogId);
}


