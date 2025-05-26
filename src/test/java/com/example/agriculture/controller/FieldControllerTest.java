package com.example.agriculture.controller;

import com.example.agriculture.AbstractIntegrationBaseTest;
import com.example.agriculture.dto.request.FieldRequest;
import com.example.agriculture.entity.FieldEntity;
import com.example.agriculture.repository.FieldRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
import com.example.agriculture.AgricultureEntrypoint;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.time.LocalDateTime;
import java.util.UUID;

@Testcontainers
@SpringBootTest(
        classes = AgricultureEntrypoint.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FieldControllerTest extends AbstractIntegrationBaseTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FieldRepository fieldRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        jdbcTemplate.execute("TRUNCATE TABLE agriculture.field RESTART IDENTITY CASCADE");
    }


    @Test
    public void field_ShouldCreateFieldAndReturnOk() throws Exception {
        FieldRequest request = new FieldRequest(
                "test",
                123.0,
                "test"
        );

        mockMvc.perform(post("/api/fields")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.area").value(123.0))
                .andExpect(jsonPath("$.soilType").value("test"));
    }

    @Test
    public void field_ShouldReturnAllFields() throws Exception {
        buildFieldEntity();
        buildFieldEntity();

        mockMvc.perform(get("/api/fields"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("test"))
                .andExpect(jsonPath("$[1].name").value("test"));
    }

    @Test
    public void field_ShouldReturnFieldById() throws Exception {
        FieldEntity saved = buildFieldEntity();

        mockMvc.perform(get("/api/fields/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.area").value(123.0))
                .andExpect(jsonPath("$.soilType").value("test"));
    }

    @Test
    public void field_ShouldReturnNotFound_WhenFieldDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/api/fields/" + fakeId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void field_ShouldDeleteFieldById() throws Exception {
        UUID fieldId = buildFieldEntity().getId();

        mockMvc.perform(delete("/api/fields/" + fieldId))
                .andExpect(status().isOk());

        boolean exists = fieldRepository.existsById(fieldId);
        Assertions.assertFalse(exists, "Поле с данным id cуществует");
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