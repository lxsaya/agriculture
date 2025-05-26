package com.example.agriculture;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AbstractIntegrationBaseTest {
    private final static GenericContainer<?> POSTGRES_CONTAINER;
    private static final String POSTGRES_DB_NAME = "postgres";

    static {
        POSTGRES_CONTAINER = new GenericContainer<>("postgres:16")
                .withEnv("POSTGRES_PASSWORD", "postgres")
                .withEnv("POSTGRES_USER", "postgres")
                .withEnv("POSTGRES_DB", POSTGRES_DB_NAME)
                .withExposedPorts(5432);
        POSTGRES_CONTAINER.start();
    }

    @DynamicPropertySource
    static void initProperties(DynamicPropertyRegistry registry) {
        initPostgres(registry);
    }

    private static void initPostgres(DynamicPropertyRegistry registry) {
        var urlPg = String.format(
                "%s:%d/%s",
                POSTGRES_CONTAINER.getHost(),
                POSTGRES_CONTAINER.getFirstMappedPort(),
                POSTGRES_DB_NAME
        );
        var jdbcUrl = "jdbc:postgresql://" + urlPg;
        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");
        registry.add("spring.liquibase.url", () -> jdbcUrl);
        registry.add("spring.liquibase.liquibase-schema", () -> "public");
    }
}

