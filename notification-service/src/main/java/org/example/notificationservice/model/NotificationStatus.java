package org.example.notificationservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of a notification delivery")
public enum NotificationStatus {
  @Schema(description = "Notification is queued and waiting to be sent")
  PENDING,

  @Schema(description = "Notification was sent successfully")
  SENT,

  @Schema(description = "Notification delivery failed")
  FAILED
}
