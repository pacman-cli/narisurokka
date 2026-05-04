package org.example.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(description = "Token refresh request")
public class RefreshTokenRequest {

  @NotBlank(message = "Refresh token is required")
  @Schema(description = "The refresh token to exchange for a new access token")
  private String refreshToken;
}
