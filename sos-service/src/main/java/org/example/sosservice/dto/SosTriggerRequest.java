package org.example.sosservice.dto;

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
@Schema(description = "Request body for triggering an SOS alert")
public class SosTriggerRequest {

  @NotNull(message = "User ID is required")
  @Schema(description = "User ID who is triggering the SOS", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID userId;

  @NotNull(message = "Latitude is required")
  @Schema(description = "Current latitude", example = "23.8103")
  private Double lat;

  @NotNull(message = "Longitude is required")
  @Schema(description = "Current longitude", example = "90.4125")
  private Double lng;
}
