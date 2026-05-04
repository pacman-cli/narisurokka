package org.example.authservice.dto;

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
@Schema(description = "Authentication token response")
public class AuthResponse {

  @Schema(description = "JWT access token")
  private String accessToken;

  @Schema(description = "Refresh token for obtaining new access tokens")
  private String refreshToken;

  @Schema(description = "Token type", example = "Bearer")
  @Builder.Default
  private String tokenType = "Bearer";

  @Schema(description = "Access token expiration in milliseconds")
  private long expiresIn;

  @Schema(description = "Authenticated user ID")
  private UUID userId;

  @Schema(description = "User email")
  private String email;

  @Schema(description = "User role")
  private String role;
}
