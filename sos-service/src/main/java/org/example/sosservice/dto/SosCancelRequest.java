package org.example.sosservice.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Request body for cancelling an SOS alert")
public class SosCancelRequest {

  @NotNull(message = "User ID is required")
  @Schema(description = "User ID who is cancelling the SOS", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID userId;

  @NotBlank(message = "Cancellation reason is required")
  @Schema(description = "Reason for cancelling the SOS", example = "False alarm - situation resolved")
  private String reason;
}
