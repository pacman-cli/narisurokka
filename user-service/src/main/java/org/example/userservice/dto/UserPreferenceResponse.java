package org.example.userservice.dto;

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
@Schema(description = "User preference response")
public class UserPreferenceResponse {

  @Schema(description = "Unique preference identifier")
  private UUID id;

  @Schema(description = "User ID this preference belongs to")
  private UUID userId;

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

  @Schema(description = "Preferred language code", example = "en")
  private String language;

  @Schema(description = "Timestamp when preferences were created")
  private Instant createdAt;

  @Schema(description = "Timestamp when preferences were last updated")
  private Instant updatedAt;
}
