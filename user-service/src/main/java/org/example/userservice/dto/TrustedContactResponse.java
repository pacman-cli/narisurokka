package org.example.userservice.dto;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Trusted contact response")
public class TrustedContactResponse {

  @Schema(description = "Contact ID")
  private String id;

  @Schema(description = "User ID")
  private String userId;

  @Schema(description = "Contact's name")
  private String contactName;

  @Schema(description = "Contact's phone number")
  private String contactPhone;

  @Schema(description = "Contact's email")
  private String contactEmail;

  @Schema(description = "Relationship to user")
  private String relationship;

  @Schema(description = "Primary contact for SOS alerts")
  private Boolean isPrimary;

  @Schema(description = "Send SMS notifications")
  private Boolean notifySms;

  @Schema(description = "Send email notifications")
  private Boolean notifyEmail;

  @Schema(description = "Send WhatsApp notifications")
  private Boolean notifyWhatsapp;

  @Schema(description = "Created timestamp")
  private Instant createdAt;
}