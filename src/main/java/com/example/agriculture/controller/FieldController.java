package com.example.agriculture.controller;

import com.example.agriculture.dto.request.FieldRequest;
import com.example.agriculture.dto.responce.FieldResponse;
import com.example.agriculture.entity.FieldEntity;
import com.example.agriculture.service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fields")
@RequiredArgsConstructor
public class FieldController {
    private final FieldService fieldService;

    @GetMapping
    public List<FieldResponse> getAll() {
        return fieldService.getAllFields();
    }

    @GetMapping("/{fieldId}")
    public FieldResponse getFieldById(@PathVariable UUID fieldId) {
        return fieldService.getFieldById(fieldId);
    }

    @PostMapping
    public FieldEntity createField(@RequestBody FieldRequest fieldRequest) {
        return fieldService.saveField(fieldRequest);
    }

    @DeleteMapping("/{fieldId}")
    public void deleteFieldById(@PathVariable UUID fieldId) {
        fieldService.deleteFieldById(fieldId);
    }

}
