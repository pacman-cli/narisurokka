package org.example.locationservice.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Request body for updating user location")
public class LocationUpdateRequest {

  @NotNull(message = "SOS ID is required")
  @Schema(description = "SOS case ID", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID sosId;

  @NotNull(message = "User ID is required")
  @Schema(description = "User ID sending location", example = "987fcdeb-51a2-3bc4-d567-890123456789")
  private UUID userId;

  @NotNull(message = "Latitude is required")
  @Schema(description = "Latitude coordinate", example = "23.8103")
  private Double lat;

  @NotNull(message = "Longitude is required")
  @Schema(description = "Longitude coordinate", example = "90.4125")
  private Double lng;
}
