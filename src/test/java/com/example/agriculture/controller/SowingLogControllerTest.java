package com.example.agriculture.controller;

import com.example.agriculture.dto.request.SowingLogRequest;
import com.example.agriculture.entity.FieldEntity;
import com.example.agriculture.entity.PlantEntity;
import com.example.agriculture.entity.SowingLogEntity;
import com.example.agriculture.entity.WorkerEntity;
import com.example.agriculture.repository.FieldRepository;
import com.example.agriculture.repository.PlantRepository;
import com.example.agriculture.repository.SowingLogRepository;
import com.example.agriculture.repository.WorkerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SowingLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SowingLogRepository sowingLogRepository;
    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private FieldRepository fieldRepository;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE agriculture.sowing_log RESTART IDENTITY CASCADE");
    }

    @Test
    public void sowingLog_ShouldCreateSowingLogAndReturnOk() throws Exception {
        UUID workerId = buildWorkerEntity().getId();
        UUID plantId = buildPlantEntity().getId();
        UUID fieldId = buildFieldEntity().getId();

        SowingLogRequest request = new SowingLogRequest(
                workerId,
                plantId,
                fieldId,
                10.5
        );

        mockMvc.perform(post("/api/sowing-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantityKg").value(10.5));
    }

    @Test
    public void sowingLog_ShouldReturnAllSowingLogs() throws Exception {
        UUID firstWorkerId = buildWorkerEntity().getId();
        UUID firstPlantId = buildPlantEntity().getId();
        UUID firstFieldId = buildFieldEntity().getId();
        UUID secondWorkerId = buildWorkerEntity().getId();
        UUID secondPlantId = buildPlantEntity().getId();
        UUID secondFieldId = buildFieldEntity().getId();
        buildSowingLogEntity(firstWorkerId, firstPlantId, firstFieldId);
        buildSowingLogEntity(secondWorkerId, secondPlantId, secondFieldId);

        mockMvc.perform(get("/api/sowing-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void sowingLog_ShouldReturnSowingLogById() throws Exception {
        UUID workerId = buildWorkerEntity().getId();
        UUID plantId = buildPlantEntity().getId();
        UUID fieldId = buildFieldEntity().getId();
        SowingLogEntity saved = buildSowingLogEntity(workerId, plantId, fieldId);

        mockMvc.perform(get("/api/sowing-logs/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId().toString()))
                .andExpect(jsonPath("$.quantityKg").value(saved.getQuantityKg()));
    }

    @Test
    public void sowingLog_ShouldReturnNotFound_WhenSowingLogDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/api/sowing-logs/" + fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void sowingLog_ShouldMarkFieldAsSown() throws Exception {
        UUID workerId = buildWorkerEntity().getId();
        UUID plantId = buildPlantEntity().getId();
        UUID fieldId = buildFieldEntity().getId();
        UUID sowingLogId = buildSowingLogEntity(workerId, plantId, fieldId).getId();

        mockMvc.perform(post("/api/sowing-logs/" + sowingLogId)
                        .param("localDate", LocalDate.now().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFieldSown").value(true));
    }

    @Test
    public void sowingLog_ShouldDeleteSowingLogById() throws Exception {
        UUID workerId = buildWorkerEntity().getId();
        UUID plantId = buildPlantEntity().getId();
        UUID fieldId = buildFieldEntity().getId();
        UUID sowingLogId = buildSowingLogEntity(workerId, plantId, fieldId).getId();

        mockMvc.perform(delete("/api/sowing-logs/" + sowingLogId))
                .andExpect(status().isOk());

        Assertions.assertFalse(sowingLogRepository.existsById(sowingLogId), "SowingLog с данным id существует");
    }

    private SowingLogEntity buildSowingLogEntity(UUID workerId, UUID plantId, UUID fieldId) {
        LocalDateTime now = LocalDateTime.now();
        SowingLogEntity entity = SowingLogEntity.builder()
                .workerId(workerId)
                .plantId(plantId)
                .fieldId(fieldId)
                .quantityKg(5.0)
                .isFieldSown(false)
                .sowingDate(LocalDate.now())
                .createdAt(now)
                .modifiedAt(now)
                .build();
        return sowingLogRepository.save(entity);
    }

    private WorkerEntity buildWorkerEntity() {
        LocalDateTime now = LocalDateTime.now();
        var workerEntity = WorkerEntity.builder()
                .fullName("test")
                .position("test")
                .createdAt(now)
                .modifiedAt(now)
                .build();
        return workerRepository.save(workerEntity);
    }

    private PlantEntity buildPlantEntity() {
        LocalDateTime now = LocalDateTime.now();
        var plantEntity = PlantEntity.builder()
                .name("test")
                .type("test")
                .growthPeriod(123)
                .createdAt(now)
                .modifiedAt(now)
                .build();
        return plantRepository.save(plantEntity);
    }

    private FieldEntity buildFieldEntity() {
        LocalDateTime now = LocalDateTime.now();
        var fieldEntity = FieldEntity.builder()
                .name("test")
                .area(123.0)
                .soilType("test")
                .createdAt(now)
                .modifiedAt(now)
                .build();
        return fieldRepository.save(fieldEntity);
    }
}
