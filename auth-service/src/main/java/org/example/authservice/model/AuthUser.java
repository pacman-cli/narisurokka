package org.example.authservice.model;

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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "auth_users", indexes = {
        @Index(name = "idx_auth_user_email", columnList = "email", unique = true),
        @Index(name = "idx_auth_user_phone", columnList = "phone", unique = true)
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Authentication user entity")
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Unique user identifier")
    private UUID id;

    @Column(nullable = false, unique = true)
    @Schema(description = "User email address", example = "user@narisurokkha.com")
    private String email;

    @Column(unique = true)
    @Schema(description = "User phone number", example = "+8801712345678")
    private String phone;

    @Column(nullable = false)
    @Schema(description = "BCrypt-encoded password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    @Schema(description = "User role")
    private Role role = Role.USER;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    @Schema(description = "Whether the account is active")
    private boolean enabled = true;

    @Column(name = "is_locked", nullable = false)
    @Builder.Default
    @Schema(description = "Whether the account is locked")
    private boolean locked = false;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private int failedLoginAttempts = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    private Long version;

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
