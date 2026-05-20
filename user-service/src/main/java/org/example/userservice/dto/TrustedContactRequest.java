package org.example.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create/update a trusted contact")
public class TrustedContactRequest {

  @NotBlank(message = "Contact name is required")
  @Schema(description = "Contact's name", example = "Rahul Ahmed")
  private String contactName;

  @NotBlank(message = "Contact phone is required")
  @Schema(description = "Contact's phone number", example = "+8801712345678")
  private String contactPhone;

  @Schema(description = "Contact's email (optional)", example = "rahul@example.com")
  private String contactEmail;

  @Schema(description = "Relationship to user", example = "Brother")
  private String relationship;

  @Schema(description = "Set as primary contact for SOS alerts")
  private Boolean isPrimary;

  @Schema(description = "Send SMS notifications")
  private Boolean notifySms;

  @Schema(description = "Send email notifications")
  private Boolean notifyEmail;

  @Schema(description = "Send WhatsApp notifications")
  private Boolean notifyWhatsapp;
}