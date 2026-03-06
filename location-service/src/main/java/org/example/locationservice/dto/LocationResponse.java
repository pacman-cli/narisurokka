package org.example.locationservice.dto;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Location response")
public class LocationResponse {

  @Schema(description = "Ping ID")
  private UUID id;

  @Schema(description = "SOS case ID")
  private UUID sosId;

  @Schema(description = "User ID")
  private UUID userId;

  @Schema(description = "Latitude", example = "23.8103")
  private double lat;

  @Schema(description = "Longitude", example = "90.4125")
  private double lng;

  @Schema(description = "Timestamp of the ping")
  private Instant timestamp;
}
