package org.example.notificationservice.dto;

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
@Schema(description = "Notification response")
public class NotificationResponse {

  @Schema(description = "Notification ID")
  private UUID id;

  @Schema(description = "Target user ID")
  private UUID userId;

  @Schema(description = "Associated SOS case ID")
  private UUID sosId;

  @Schema(description = "Notification type (EMAIL, SMS, PUSH)")
  private String type;

  @Schema(description = "Delivery status (PENDING, SENT, FAILED)")
  private String status;

  @Schema(description = "Subject/title")
  private String subject;

  @Schema(description = "Message body")
  private String message;

  @Schema(description = "Recipient address")
  private String recipientAddress;

  @Schema(description = "Created timestamp")
  private Instant createdAt;

  @Schema(description = "Sent timestamp")
  private Instant sentAt;
}
