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
@Schema(description = "Request body for resolving an SOS alert")
public class SosResolveRequest {

  @NotNull(message = "User ID is required")
  @Schema(description = "User ID whose SOS is being resolved", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID userId;

  @Schema(description = "Notes from the responder", example = "Authorities arrived, user is safe")
  private String resolutionNotes;
}
