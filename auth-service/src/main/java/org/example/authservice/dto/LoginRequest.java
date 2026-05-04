package org.example.authservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "Login request")
public class LoginRequest {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  @Schema(description = "User email", example = "user@narisurokkha.com")
  private String email;

  @NotBlank(message = "Password is required")
  @Schema(description = "User password")
  private String password;
}
