package org.example.notificationservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Channel through which the notification is delivered")
public enum NotificationType {
  @Schema(description = "Email notification")
  EMAIL,

  @Schema(description = "SMS text message")
  SMS,

  @Schema(description = "Push notification to mobile device")
  PUSH
}
