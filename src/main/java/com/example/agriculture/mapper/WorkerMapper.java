package com.example.agriculture.mapper;

import com.example.agriculture.dto.responce.WorkerResponse;
import com.example.agriculture.entity.WorkerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkerMapper {
    WorkerResponse toResponse(WorkerEntity workerEntity);
}
