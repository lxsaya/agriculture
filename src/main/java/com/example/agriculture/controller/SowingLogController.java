package com.example.agriculture.controller;

import com.example.agriculture.dto.request.SowingLogRequest;
import com.example.agriculture.dto.responce.SowingLogResponse;
import com.example.agriculture.entity.SowingLogEntity;
import com.example.agriculture.service.SowingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sowing-logs")
@RequiredArgsConstructor
public class SowingLogController {
    private final SowingLogService sowingLogService;

    @GetMapping
    public List<SowingLogResponse> getAllSowingLogs(@RequestParam(required = false) UUID plantId,
                                          @RequestParam(required = false) String date) {
        return sowingLogService.getSowingLogs(plantId, date);
    }

    @PostMapping("/{sowingLogId}")
    public SowingLogResponse markFieldAsSown(@PathVariable UUID sowingLogId, @RequestParam String localDate) {
        return sowingLogService.markFieldAsSown(sowingLogId, localDate);
    }

    @GetMapping("/{sowingLogId}")
    public SowingLogResponse getSowingLogById(@PathVariable UUID sowingLogId) {
        return sowingLogService.getSowingLogById(sowingLogId);
    }

    @PostMapping
    public SowingLogEntity createSowingLog(@RequestBody SowingLogRequest sowingLogRequest) {
        return sowingLogService.saveSowingLog(sowingLogRequest);
    }

    @DeleteMapping("/{sowingLogId}")
    public void deleteSowingLogById(@PathVariable UUID sowingLogId) {
        sowingLogService.deleteSowingLogById(sowingLogId);
    }
}
