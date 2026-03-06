package org.example.locationservice.model;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "location_pings", indexes = {
        @Index(name = "idx_location_sos_id", columnList = "sos_id"),
        @Index(name = "idx_location_user_id", columnList = "user_id"),
        @Index(name = "idx_location_timestamp", columnList = "timestamp")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "A single GPS location ping during an SOS event")
public class LocationPing {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique ping identifier")
    private UUID id;

    @Column(name = "sos_id", nullable = false)
    @Schema(description = "SOS case this location belongs to", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID sosId;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "User who sent this location", example = "987fcdeb-51a2-3bc4-d567-890123456789")
    private UUID userId;

    @Column(nullable = false)
    @Schema(description = "Latitude coordinate", example = "23.8103")
    private double lat;

    @Column(nullable = false)
    @Schema(description = "Longitude coordinate", example = "90.4125")
    private double lng;

    @Column(nullable = false)
    @Schema(description = "Timestamp of the location ping")
    private Instant timestamp;

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = Instant.now();
        }
    }
}
