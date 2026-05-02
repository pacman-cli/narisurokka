package org.example.incidentservice.dto;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Incident response")
public class IncidentResponse {

  @Schema(description = "Incident ID")
  private String id;

  @Schema(description = "Reporter ID (null if anonymous)")
  private String reporterId;

  @Schema(description = "Is anonymous report")
  private Boolean anonymousReport;

  @Schema(description = "Incident type")
  private String incidentType;

  @Schema(description = "Description")
  private String description;

  @Schema(description = "Location address")
  private String locationAddress;

  @Schema(description = "Latitude")
  private Double lat;

  @Schema(description = "Longitude")
  private Double lng;

  @Schema(description = "Current status")
  private String status;

  @Schema(description = "When incident occurred")
  private Instant incidentDate;

  @Schema(description = "Number of witnesses")
  private Integer witnessCount;

  @Schema(description = "Evidence URLs")
  private List<String> evidenceUrls;

  @Schema(description = "Anonymous to authorities")
  private Boolean isAnonymousToAuthorities;

  @Schema(description = "Resolution notes")
  private String resolvedNotes;

  @Schema(description = "Created at")
  private Instant createdAt;

  @Schema(description = "Updated at")
  private Instant updatedAt;

  @Schema(description = "Resolved at")
  private Instant resolvedAt;
}