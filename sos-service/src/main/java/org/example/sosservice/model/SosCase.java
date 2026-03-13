package org.example.sosservice.model;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sos_case")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "SOS Case entity representing an emergency alert")
public class SosCase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique identifier for the SOS case", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID id;

    @Column(nullable = false)
    @Schema(description = "User ID who triggered the SOS", example = "987fcdeb-51a2-3bc4-d567-890123456789")
    private UUID userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Current status of the SOS case", example = "ACTIVE")
    private SosStatus status;

    @Column(nullable = false)
    @Schema(description = "Timestamp when SOS was triggered", example = "2026-01-21T08:30:00Z")
    private Instant triggeredAt;

    @Schema(description = "Timestamp when SOS was resolved/cancelled", example = "2026-01-21T09:00:00Z")
    private Instant resolvedAt;

    @Schema(description = "Reason for cancellation if cancelled", example = "False alarm")
    private String cancelReason;

    @Schema(description = "Notes when SOS was resolved")
    private String resolutionNotes;

    @Version
    @Schema(description = "Version for optimistic locking", hidden = true)
    private Integer version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
// @Schema -> Swagger
