package org.example.notificationservice.model;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notification_user_id", columnList = "user_id"),
    @Index(name = "idx_notification_sos_id", columnList = "sos_id"),
    @Index(name = "idx_notification_status", columnList = "status")
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "A notification record")
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique notification identifier")
  private UUID id;

  @Column(name = "user_id", nullable = false)
  @Schema(description = "Target user ID")
  private UUID userId;

  @Column(name = "sos_id")
  @Schema(description = "Associated SOS case ID (if triggered by SOS)")
  private UUID sosId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Schema(description = "Notification channel type")
  private NotificationType type;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Schema(description = "Delivery status")
  private NotificationStatus status;

  @Column(nullable = false)
  @Schema(description = "Notification subject/title")
  private String subject;

  @Column(columnDefinition = "TEXT", nullable = false)
  @Schema(description = "Notification body/content")
  private String message;

  @Column(name = "recipient_address")
  @Schema(description = "Recipient address (email, phone, device token)")
  private String recipientAddress;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Schema(description = "When the notification was created")
  private Instant createdAt;

  @Column(name = "sent_at")
  @Schema(description = "When the notification was actually sent")
  private Instant sentAt;

  @Column(name = "error_message")
  @Schema(description = "Error message if delivery failed")
  private String errorMessage;

  @PrePersist
  protected void onCreate() {
    if (this.createdAt == null) {
      this.createdAt = Instant.now();
    }
    if (this.status == null) {
      this.status = NotificationStatus.PENDING;
    }
  }
}
