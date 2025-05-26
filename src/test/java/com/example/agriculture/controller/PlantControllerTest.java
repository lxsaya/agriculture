package com.example.agriculture.controller;

import com.example.agriculture.AbstractIntegrationBaseTest;
import com.example.agriculture.AgricultureEntrypoint;
import com.example.agriculture.dto.request.PlantRequest;
import com.example.agriculture.entity.PlantEntity;
import com.example.agriculture.repository.PlantRepository;
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
public class PlantControllerTest extends AbstractIntegrationBaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE agriculture.plant RESTART IDENTITY CASCADE");
    }


    @Test
    public void plant_ShouldCreatePlantAndReturnOk() throws Exception {
        PlantRequest request = new PlantRequest(
                "test",
                "test",
                123

        );

        mockMvc.perform(post("/api/plants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.type").value("test"))
                .andExpect(jsonPath("$.growthPeriod").value(123));
    }

    @Test
    public void plant_ShouldReturnAllPlants() throws Exception {
        buildPlantEntity();
        buildPlantEntity();

        mockMvc.perform(get("/api/plants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("test"))
                .andExpect(jsonPath("$[1].name").value("test"));
    }

    @Test
    public void plant_ShouldReturnPlantById() throws Exception {
        PlantEntity saved = buildPlantEntity();

        mockMvc.perform(get("/api/plants/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.type").value("test"))
                .andExpect(jsonPath("$.growthPeriod").value(123));
    }

    @Test
    public void plant_ShouldReturnNotFound_WhenPlantDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/api/plants/" + fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void plant_ShouldDeletePlantById() throws Exception {
        UUID plantId = buildPlantEntity().getId();

        mockMvc.perform(delete("/api/plants/" + plantId))
                .andExpect(status().isOk());

        boolean exists = plantRepository.existsById(plantId);
        Assertions.assertFalse(exists, "Растение с данным id cуществует");
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
}