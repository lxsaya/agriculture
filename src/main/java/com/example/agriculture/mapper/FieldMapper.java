package com.example.agriculture.mapper;

import com.example.agriculture.dto.responce.FieldResponse;
import com.example.agriculture.entity.FieldEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FieldMapper {
    FieldResponse toResponse(FieldEntity fieldEntity);
}
