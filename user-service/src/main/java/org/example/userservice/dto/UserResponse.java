package org.example.userservice.dto;

import java.time.Instant;
import java.time.LocalDate;
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
@Schema(description = "User profile response")
public class UserResponse {

  @Schema(description = "Unique user identifier", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Full name of the user", example = "Fatima Akter")
  private String fullName;

  @Schema(description = "Email address", example = "fatima@example.com")
  private String email;

  @Schema(description = "Phone number with country code", example = "+8801712345678")
  private String phone;

  @Schema(description = "Date of birth", example = "1998-06-15")
  private LocalDate dateOfBirth;

  @Schema(description = "URL to profile photo", example = "https://storage.example.com/photos/user123.jpg")
  private String profilePhoto;

  @Schema(description = "Gender of the user", example = "FEMALE")
  private String gender;

  @Schema(description = "Blood group", example = "B+")
  private String bloodGroup;

  @Schema(description = "Home address", example = "123, Dhanmondi, Dhaka")
  private String address;

  @Schema(description = "Whether the user account is active", example = "true")
  private Boolean isActive;

  @Schema(description = "Timestamp when user was created")
  private Instant createdAt;

  @Schema(description = "Timestamp when user was last updated")
  private Instant updatedAt;
}
