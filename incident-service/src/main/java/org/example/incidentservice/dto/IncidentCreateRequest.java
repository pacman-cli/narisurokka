package org.example.incidentservice.dto;

import java.time.Instant;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create an incident report")
public class IncidentCreateRequest {

  @Schema(description = "Reporter user ID (optional - can be null for anonymous)")
  private String reporterId;

  @Schema(description = "Submit anonymously")
  private Boolean anonymousReport;

  @NotNull(message = "Incident type is required")
  @Schema(description = "Type of incident", example = "HARASSMENT")
  private String incidentType;

  @NotBlank(message = "Description is required")
  @Schema(description = "Detailed description", example = "Someone followed me for several blocks")
  private String description;

  @Schema(description = "Location description", example = "Near Dhanmondi Lake, Dhaka")
  private String locationAddress;

  @Schema(description = "Latitude")
  private Double lat;

  @Schema(description = "Longitude")
  private Double lng;

  @Schema(description = "When incident occurred")
  private Instant incidentDate;

  @Schema(description = "Number of witnesses")
  private Integer witnessCount;

  @Schema(description = "URLs to evidence (comma-separated)")
  private List<String> evidenceUrls;

  @Schema(description = "Keep anonymous from authorities")
  private Boolean isAnonymousToAuthorities;
}