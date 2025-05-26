package com.example.agriculture.mapper;

import com.example.agriculture.dto.responce.SowingLogResponse;
import com.example.agriculture.entity.SowingLogEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SowingLogMapper {
    SowingLogResponse toResponse(SowingLogEntity sowingLogEntity);
}
