package com.example.agriculture.service;

import com.example.agriculture.dto.request.SowingLogRequest;
import com.example.agriculture.dto.responce.SowingLogResponse;
import com.example.agriculture.entity.FieldEntity;
import com.example.agriculture.entity.PlantEntity;
import com.example.agriculture.entity.SowingLogEntity;
import com.example.agriculture.entity.WorkerEntity;
import com.example.agriculture.mapper.SowingLogMapper;
import com.example.agriculture.repository.FieldRepository;
import com.example.agriculture.repository.PlantRepository;
import com.example.agriculture.repository.SowingLogRepository;
import com.example.agriculture.repository.WorkerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SowingLogService {
    private final SowingLogRepository sowingLogRepository;
    private final PlantRepository plantRepository;
    private final FieldRepository fieldRepository;
    private final WorkerRepository workerRepository;

    private final SowingLogMapper sowingLogMapper;

    @Transactional(readOnly = true)
    public List<SowingLogResponse> getSowingLogs(UUID plantId, String date) {
        List<SowingLogEntity> SowingLogEntities;

        if (plantId != null) {
            SowingLogEntities = sowingLogRepository.findByPlantId(plantId);
        } else if (date != null && !date.isBlank()) {
            LocalDate localDate = LocalDate.parse(date);
            SowingLogEntities = sowingLogRepository.findBySowingDate(localDate);
        } else {
            SowingLogEntities = sowingLogRepository.findAll();
        }

        return SowingLogEntities
                .stream()
                .map(sowingLogMapper::toResponse)
                .collect(Collectors.toList());
    }

/*    public List<SowingLogResponse> getAllSowingLogs() {
        return sowingLogRepository.findAll()
                .stream()
                .map(sowingLogMapper::toResponse)
                .collect(Collectors.toList());
    }*/

    public SowingLogResponse markFieldAsSown(UUID sowingLogId, String date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate sowingDate = LocalDate.parse(date);
        SowingLogEntity sowingLogEntity = sowingLogRepository.findById(sowingLogId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sowing log didn't found: " + sowingLogId
                ));
        sowingLogEntity.setSowingDate(sowingDate);
        sowingLogEntity.setIsFieldSown(true);
        sowingLogEntity.setModifiedAt(now);
        SowingLogEntity saved = sowingLogRepository.save(sowingLogEntity);
        return sowingLogMapper.toResponse(saved);
    }

    public SowingLogResponse getSowingLogById(UUID id) {
        return sowingLogRepository.findById(id)
                .map(sowingLogMapper::toResponse)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Sowing log not found")
                );
    }

    @Transactional
    public SowingLogEntity saveSowingLog(SowingLogRequest sowingLogRequest) {
        SowingLogEntity sowingLogEntity = buildSowingLogEntity(sowingLogRequest);
        return sowingLogRepository.save(sowingLogEntity);
    }

    @Transactional
    public void deleteSowingLogById(UUID id) {
        if (!sowingLogRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sowing log not found");
        }
        sowingLogRepository.deleteById(id);
    }

    private SowingLogEntity buildSowingLogEntity(SowingLogRequest sowingLogRequest) {
        LocalDateTime now = LocalDateTime.now();
        return SowingLogEntity.builder()
                .workerId(sowingLogRequest.workerId())
                .plantId(sowingLogRequest.plantId())
                .fieldId(sowingLogRequest.fieldId())
                .quantityKg(sowingLogRequest.quantityKg())
                .createdAt(now)
                .modifiedAt(now)
                .build();
    }
}
