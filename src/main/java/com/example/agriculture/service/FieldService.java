package com.example.agriculture.service;

import com.example.agriculture.dto.request.FieldRequest;
import com.example.agriculture.dto.responce.FieldResponse;
import com.example.agriculture.entity.FieldEntity;
import com.example.agriculture.mapper.FieldMapper;
import com.example.agriculture.repository.FieldRepository;
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
public class FieldService {
    private final FieldRepository fieldRepository;
    private final FieldMapper fieldMapper;

    public List<FieldResponse> getAllFields() {
        return fieldRepository.findAll()
                .stream()
                .map(fieldMapper::toResponse)
                .collect(Collectors.toList());
    }

    public FieldResponse getFieldById(UUID id) {
        return fieldRepository.findById(id)
                .map(fieldMapper::toResponse)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found")
                );
    }

    @Transactional
    public FieldEntity saveField(FieldRequest fieldRequest) {
        FieldEntity fieldEntity = buildFieldEntity(fieldRequest);
        return fieldRepository.save(fieldEntity);
    }

    @Transactional
    public void deleteFieldById(UUID id) {
        if (!fieldRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Field not found");
        }
        fieldRepository.deleteById(id);
    }

    public FieldEntity buildFieldEntity(FieldRequest fieldRequest) {
        LocalDateTime now = LocalDateTime.now();
        return FieldEntity.builder()
                .name(fieldRequest.name())
                .area(fieldRequest.area())
                .soilType(fieldRequest.soilType())
                .createdAt(now)
                .modifiedAt(now)
                .build();
    }
}
