package org.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request body for updating user preferences")
public class UserPreferenceRequest {

  @Schema(description = "Enable live GPS tracking during emergencies", example = "true")
  private Boolean enableLiveTracking;

  @Schema(description = "Enable SOS trigger by shaking the device", example = "false")
  private Boolean sosShakeTrigger;

  @Schema(description = "Enable SMS notifications", example = "true")
  private Boolean notificationSms;

  @Schema(description = "Enable email notifications", example = "true")
  private Boolean notificationEmail;

  @Schema(description = "Enable push notifications", example = "true")
  private Boolean notificationPush;

  @Size(min = 2, max = 10, message = "Language code must be between 2 and 10 characters")
  @Schema(description = "Preferred language code", example = "en")
  private String language;
}
