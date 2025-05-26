package com.example.agriculture.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.Id;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "agriculture", name = "plant")
@Builder
public class PlantEntity implements Persistable<UUID> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @NotNull
    private UUID id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @Column(name = "growth_period")
    private Integer growthPeriod;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @NotNull
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Override
    public boolean isNew() {
        return id == null;
    }
}
