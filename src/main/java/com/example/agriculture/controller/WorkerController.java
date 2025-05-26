package com.example.agriculture.controller;

import com.example.agriculture.dto.request.WorkerRequest;
import com.example.agriculture.dto.responce.WorkerResponse;
import com.example.agriculture.entity.WorkerEntity;
import com.example.agriculture.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @GetMapping
    public List<WorkerResponse> getAll() {
        return workerService.getAllWorkers();
    }

    @GetMapping("/{workerId}")
    public WorkerResponse getWorkerById(@PathVariable UUID workerId) {
        return workerService.getWorkerById(workerId);
    }

    @PostMapping
    public WorkerEntity createWorker(@RequestBody WorkerRequest workerRequest) {
        return workerService.saveWorker(workerRequest);
    }

    @DeleteMapping("/{workerId}")
    public void deleteWorkerById(@PathVariable UUID workerId) {
        workerService.deleteWorkerById(workerId);
    }
}
