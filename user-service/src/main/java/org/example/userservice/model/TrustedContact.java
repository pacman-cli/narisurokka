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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trusted_contacts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Trusted emergency contact for a user")
public class TrustedContact {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique contact identifier")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @Schema(description = "User who owns this contact")
  private User user;

  @Column(name = "contact_name", nullable = false, length = 100)
  @Schema(description = "Contact's name", example = "Rahul Ahmed")
  private String contactName;

  @Column(name = "contact_phone", nullable = false, length = 20)
  @Schema(description = "Contact's phone number", example = "+8801712345678")
  private String contactPhone;

  @Column(name = "contact_email", length = 150)
  @Schema(description = "Contact's email (optional)", example = "rahul@example.com")
  private String contactEmail;

  @Column(name = "relationship", length = 50)
  @Schema(description = "Relationship to user", example = "Brother")
  private String relationship;

  @Column(name = "is_primary", nullable = false)
  @Builder.Default
  @Schema(description = "Primary contact for SOS alerts")
  private Boolean isPrimary = false;

  @Column(name = "notify_sms", nullable = false)
  @Builder.Default
  @Schema(description = "Send SMS notifications")
  private Boolean notifySms = true;

  @Column(name = "notify_email", nullable = false)
  @Builder.Default
  @Schema(description = "Send email notifications")
  private Boolean notifyEmail = true;

  @Column(name = "notify_whatsapp", nullable = false)
  @Builder.Default
  @Schema(description = "Send WhatsApp notifications")
  private Boolean notifyWhatsapp = false;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "deleted_at")
  @Schema(description = "Timestamp when contact was soft-deleted")
  private Instant deletedAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = Instant.now();
  }
}