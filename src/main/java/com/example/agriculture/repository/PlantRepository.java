package com.example.agriculture.repository;

import com.example.agriculture.entity.PlantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PlantRepository extends JpaRepository<PlantEntity, UUID> {

}
