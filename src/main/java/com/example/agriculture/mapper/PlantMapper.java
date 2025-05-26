package com.example.agriculture.mapper;

import com.example.agriculture.dto.responce.PlantResponse;
import com.example.agriculture.entity.PlantEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlantMapper {
    PlantResponse toResponse(PlantEntity plantEntity);
}
