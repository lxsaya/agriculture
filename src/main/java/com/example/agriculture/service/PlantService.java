package com.example.agriculture.service;

import com.example.agriculture.dto.request.PlantRequest;
import com.example.agriculture.dto.responce.PlantResponse;
import com.example.agriculture.entity.FieldEntity;
import com.example.agriculture.entity.PlantEntity;
import com.example.agriculture.mapper.PlantMapper;
import com.example.agriculture.repository.PlantRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;

    public List<PlantResponse> getAllPlants() {
        return plantRepository.findAll()
                .stream()
                .map(plantMapper::toResponse)
                .collect(Collectors.toList());
    }

    public PlantResponse getPlantById(UUID id) {
        return plantRepository.findById(id)
                .map(plantMapper::toResponse)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Plant not found")
                );

    }

    @Transactional
    public PlantEntity savePlant(PlantRequest plantRequest) {
        PlantEntity plantEntity = buildPlantEntity(plantRequest);
        return plantRepository.save(plantEntity);
    }

    @Transactional
    public void deletePlantById(UUID id) {
        if (!plantRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plant not found");
        }
        plantRepository.deleteById(id);
    }

    private PlantEntity buildPlantEntity(PlantRequest plantRequest) {
        LocalDateTime now = LocalDateTime.now();
        return PlantEntity.builder()
                .name(plantRequest.name())
                .type(plantRequest.type())
                .growthPeriod(plantRequest.growthPeriod())
                .createdAt(now)
                .modifiedAt(now)
                .build();
    }
}
