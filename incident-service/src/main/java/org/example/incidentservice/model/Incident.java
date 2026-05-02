package org.example.incidentservice.model;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "incidents")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Incident report entity")
public class Incident {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique incident identifier")
  private UUID id;

  @Column(name = "reporter_id")
  @Schema(description = "Reporter user ID (null for anonymous)")
  private UUID reporterId;

  @Column(name = "anonymous_report", nullable = false)
  @Builder.Default
  @Schema(description = "Whether report is anonymous")
  private Boolean anonymousReport = false;

  @Enumerated(EnumType.STRING)
  @Column(name = "incident_type", nullable = false, length = 30)
  @Schema(description = "Type of incident")
  private IncidentType incidentType;

  @Column(name = "description", columnDefinition = "TEXT", nullable = false)
  @Schema(description = "Detailed description of the incident")
  private String description;

  @Column(name = "location_address", length = 500)
  @Schema(description = "Location description/address where incident occurred")
  private String locationAddress;

  @Column(name = "incident_lat")
  @Schema(description = "Latitude where incident occurred")
  private Double incidentLat;

  @Column(name = "incident_lng")
  @Schema(description = "Longitude where incident occurred")
  private Double incidentLng;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 20)
  @Builder.Default
  @Schema(description = "Current incident status")
  private IncidentStatus status = IncidentStatus.SUBMITTED;

  @Column(name = "incident_date")
  @Schema(description = "When the incident occurred")
  private Instant incidentDate;

  @Column(name = "witness_count")
  @Schema(description = "Number of witnesses")
  private Integer witnessCount;

  @Column(name = "evidence_urls", columnDefinition = "TEXT")
  @Schema(description = "Comma-separated URLs to evidence")
  private String evidenceUrls;

  @Column(name = "is_anonymous_to_authorities", nullable = false)
  @Builder.Default
  @Schema(description = "Keep reporter anonymous from authorities")
  private Boolean isAnonymousToAuthorities = true;

  @Column(name = "resolved_notes", columnDefinition = "TEXT")
  @Schema(description = "Resolution notes (for authorities)")
  private String resolvedNotes;

  @Column(name = "assigned_authority_id")
  @Schema(description = "Authority handling this incident")
  private UUID assignedAuthorityId;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @Column(name = "resolved_at")
  private Instant resolvedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
    if (this.incidentDate == null) {
      this.incidentDate = Instant.now();
    }
  }
}