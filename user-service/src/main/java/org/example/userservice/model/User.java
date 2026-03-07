package org.example.userservice.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "User entity representing a registered user on NariSurokkha")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Schema(description = "Unique user identifier", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Column(name = "full_name", nullable = false, length = 100)
  @Schema(description = "Full name of the user", example = "Fatima Akter")
  private String fullName;

  @Column(nullable = false, unique = true, length = 150)
  @Schema(description = "Email address", example = "fatima@example.com")
  private String email;

  @Column(nullable = false, unique = true, length = 20)
  @Schema(description = "Phone number with country code", example = "+8801712345678")
  private String phone;

  @Column(name = "date_of_birth")
  @Schema(description = "Date of birth", example = "1998-06-15")
  private LocalDate dateOfBirth;

  @Column(name = "profile_photo", length = 500)
  @Schema(description = "URL to profile photo", example = "https://storage.example.com/photos/user123.jpg")
  private String profilePhoto;

  @Column(length = 20)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  @Schema(description = "Gender of the user", example = "FEMALE")
  private Gender gender = Gender.FEMALE;

  @Column(name = "blood_group", length = 5)
  @Schema(description = "Blood group", example = "B+")
  private String bloodGroup;

  @Column(columnDefinition = "TEXT")
  @Schema(description = "Home address", example = "123, Dhanmondi, Dhaka")
  private String address;

  @Column(name = "is_active", nullable = false)
  @Builder.Default
  @Schema(description = "Whether the user account is active", example = "true")
  private Boolean isActive = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  @Schema(description = "Timestamp when user was created")
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  @Schema(description = "Timestamp when user was last updated")
  private Instant updatedAt;

  @Column(name = "deleted_at")
  @Schema(description = "Timestamp when user was soft-deleted (null if active)")
  private Instant deletedAt;

  @Version
  @Schema(description = "Version for optimistic locking", hidden = true)
  private Integer version;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  @Schema(hidden = true)
  private UserPreference preference;

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
