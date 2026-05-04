package org.example.authservice.authentication;
import java.util.UUID;
import org.example.authservice.authentication.AuthResponse;
import org.example.authservice.authentication.LoginRequest;
import org.example.authservice.authentication.RefreshTokenRequest;
import org.example.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Login, Logout and Token Refresh endpoints")
public class AuthenticationController {
    private final AuthService authService; // Ideally inject AuthenticationService here in the future
    @Operation(summary = "Login", description = "Authenticates user with email and password. "
            + "Returns JWT access token and refresh token. "
            + "Account locks after 5 failed attempts.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or account locked")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Refresh access token", description = "Exchanges a valid refresh token for a new access token and refresh token (token rotation). "
            + "The old refresh token is revoked.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid, expired, or revoked refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Logout", description = "Revokes all refresh tokens for the authenticated user. "
            + "Requires a valid JWT access token in the Authorization header.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logged out successfully"),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        authService.logout(userId);
        return ResponseEntity.noContent().build();
    }
}
