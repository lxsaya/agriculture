package com.example.agriculture.repository;

import com.example.agriculture.entity.WorkerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkerRepository extends JpaRepository<WorkerEntity, UUID> {
    void deleteById(UUID id);
    Optional<WorkerEntity> findById(UUID id);
}
