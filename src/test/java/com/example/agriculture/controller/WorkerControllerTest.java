package com.example.agriculture.controller;

import com.example.agriculture.dto.request.WorkerRequest;
import com.example.agriculture.entity.WorkerEntity;
import com.example.agriculture.repository.WorkerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.agriculture.AbstractIntegrationBaseTest;
import com.example.agriculture.AgricultureEntrypoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest(
        classes = AgricultureEntrypoint.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class WorkerControllerTest extends AbstractIntegrationBaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE agriculture.worker RESTART IDENTITY CASCADE");
    }


    @Test
    public void worker_ShouldCreateWorkerAndReturnOk() throws Exception {
        WorkerRequest request = new WorkerRequest(
                "test",
                "test"

        );

        mockMvc.perform(post("/api/workers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("test"))
                .andExpect(jsonPath("$.position").value("test"));
    }

    @Test
    public void worker_ShouldReturnAllWorkers() throws Exception {
        buildWorkerEntity();
        buildWorkerEntity();

        mockMvc.perform(get("/api/workers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fullName").value("test"))
                .andExpect(jsonPath("$[1].position").value("test"));
    }

    @Test
    public void worker_ShouldReturnWorkerById() throws Exception {
        WorkerEntity saved = buildWorkerEntity();

        mockMvc.perform(get("/api/workers/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("test"))
                .andExpect(jsonPath("$.position").value("test"));
    }

    @Test
    public void worker_ShouldReturnNotFound_WhenWorkerDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/api/workers/" + fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void worker_ShouldDeleteWorkerById() throws Exception {
        UUID workerId = buildWorkerEntity().getId();

        mockMvc.perform(delete("/api/workers/" + workerId))
                .andExpect(status().isOk());

        boolean exists = workerRepository.existsById(workerId);
        Assertions.assertFalse(exists, "Работник с данным id cуществует");
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
}