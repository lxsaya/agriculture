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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "agriculture", name = "sowing_log")
@Builder
public class SowingLogEntity implements Persistable<UUID> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @NotNull
    private UUID id;

    @Column(name = "worker_id")
    private UUID workerId;

    @Column(name = "plant_id")
    private UUID plantId;

    @Column(name = "field_id")
    private UUID fieldId;

    @Column(name = "is_field_sown")
    private Boolean isFieldSown;

    private LocalDate sowingDate;

    private Double quantityKg;

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
