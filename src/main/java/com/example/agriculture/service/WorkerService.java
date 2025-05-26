package com.example.agriculture.service;

import com.example.agriculture.dto.request.PlantRequest;
import com.example.agriculture.dto.request.WorkerRequest;
import com.example.agriculture.dto.responce.PlantResponse;
import com.example.agriculture.dto.responce.WorkerResponse;
import com.example.agriculture.entity.PlantEntity;
import com.example.agriculture.entity.WorkerEntity;
import com.example.agriculture.mapper.WorkerMapper;
import com.example.agriculture.repository.WorkerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final WorkerMapper workerMapper;

    public List<WorkerResponse> getAllWorkers() {
        return workerRepository.findAll()
                .stream()
                .map(workerMapper::toResponse)
                .collect(Collectors.toList());
    }

    public WorkerResponse getWorkerById(UUID id) {
        return workerRepository.findById(id)
                .map(workerMapper::toResponse)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker not found")
                );
    }

    @Transactional
    public WorkerEntity saveWorker(WorkerRequest workerRequest) {
        WorkerEntity workerEntity = buildWorkerEntity(workerRequest);
        return workerRepository.save(workerEntity);
    }

    @Transactional
    public void deleteWorkerById(UUID id) {
        if (!workerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker not found");
        }
        workerRepository.deleteById(id);
    }

    private WorkerEntity buildWorkerEntity(WorkerRequest workerRequest) {
        LocalDateTime now = LocalDateTime.now();
        return WorkerEntity.builder()
                .fullName(workerRequest.fullName())
                .position(workerRequest.position())
                .createdAt(now)
                .modifiedAt(now)
                .build();
    }
}
