package org.example.userservice.model;

import java.time.Instant;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User preference settings for notifications and safety features")
public class UserPreference {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique preference identifier")
  private UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  @Schema(hidden = true)
  private User user;

  @Column(name = "enable_live_tracking", nullable = false)
  @Builder.Default
  @Schema(description = "Enable live GPS tracking during emergencies", example = "true")
  private Boolean enableLiveTracking = true;

  @Column(name = "sos_shake_trigger", nullable = false)
  @Builder.Default
  @Schema(description = "Enable SOS trigger by shaking the device", example = "false")
  private Boolean sosShakeTrigger = false;

  @Column(name = "notification_sms", nullable = false)
  @Builder.Default
  @Schema(description = "Enable SMS notifications", example = "true")
  private Boolean notificationSms = true;

  @Column(name = "notification_email", nullable = false)
  @Builder.Default
  @Schema(description = "Enable email notifications", example = "true")
  private Boolean notificationEmail = true;

  @Column(name = "notification_push", nullable = false)
  @Builder.Default
  @Schema(description = "Enable push notifications", example = "true")
  private Boolean notificationPush = true;

  @Column(length = 10, nullable = false)
  @Builder.Default
  @Schema(description = "Preferred language code", example = "en")
  private String language = "en";

  @Column(name = "created_at", nullable = false, updatable = false)
  @Schema(description = "Timestamp when preferences were created")
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  @Schema(description = "Timestamp when preferences were last updated")
  private Instant updatedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }
}
