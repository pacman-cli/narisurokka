package org.example.userservice.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Request body for user registration")
public class UserRegistrationRequest {

  @NotBlank(message = "Full name is required")
  @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
  @Schema(description = "Full name of the user", example = "Fatima Akter")
  private String fullName;

  @NotBlank(message = "Email is required")
  @Email(message = "Email must be valid")
  @Schema(description = "Email address", example = "fatima@example.com")
  private String email;

  @NotBlank(message = "Phone number is required")
  @Size(min = 10, max = 20, message = "Phone number must be between 10 and 20 characters")
  @Schema(description = "Phone number with country code", example = "+8801712345678")
  private String phone;

  @Past(message = "Date of birth must be in the past")
  @Schema(description = "Date of birth", example = "1998-06-15")
  private LocalDate dateOfBirth;

  @Schema(description = "URL to profile photo", example = "https://storage.example.com/photos/user123.jpg")
  private String profilePhoto;

  @Schema(description = "Gender of the user", example = "FEMALE")
  private String gender;

  @Size(max = 5, message = "Blood group must be at most 5 characters")
  @Schema(description = "Blood group", example = "B+")
  private String bloodGroup;

  @Schema(description = "Home address", example = "123, Dhanmondi, Dhaka")
  private String address;
}
