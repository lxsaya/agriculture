package com.example.agriculture.controller;

import com.example.agriculture.dto.request.PlantRequest;
import com.example.agriculture.dto.responce.PlantResponse;
import com.example.agriculture.entity.PlantEntity;
import com.example.agriculture.service.PlantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/plants")
@RequiredArgsConstructor
public class PlantController {
    private final PlantService plantService;

    @GetMapping
    public List<PlantResponse> getAllPlants() {
        return plantService.getAllPlants();
    }

    @GetMapping("/{plantId}")
    public PlantResponse getPlantById(@PathVariable UUID plantId) {
        return plantService.getPlantById(plantId);
    }

    @PostMapping
    public PlantEntity createPlant(@RequestBody PlantRequest plantRequest) {
        return plantService.savePlant(plantRequest);
    }

    @DeleteMapping("/{plantId}")
    public void deletePlantById(@PathVariable UUID plantId) {
        plantService.deletePlantById(plantId);
    }
}
