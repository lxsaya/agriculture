package com.example.agriculture.repository;

import com.example.agriculture.entity.FieldEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FieldRepository extends JpaRepository<FieldEntity, UUID> {
    boolean existsById(UUID fieldId);
}
