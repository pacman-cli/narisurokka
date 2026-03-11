package org.example.userservice.controller;

import java.util.UUID;

import org.example.userservice.dto.UserPreferenceRequest;
import org.example.userservice.dto.UserPreferenceResponse;
import org.example.userservice.dto.UserRegistrationRequest;
import org.example.userservice.dto.UserResponse;
import org.example.userservice.dto.UserUpdateRequest;
import org.example.userservice.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "User registration, profile management, and preferences")
public class UserController {

  private final UserServiceImpl userService;

  // ===================== User Profile Endpoints =====================

  @Operation(summary = "Register a new user", description = "Registers a new user on the NariSurokkha platform. "
      + "Creates default preferences and publishes USER_REGISTERED event to Kafka.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request data"),
      @ApiResponse(responseCode = "409", description = "User with the same email or phone already exists")
  })
  @PostMapping("/register")
  public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
    UserResponse response = userService.registerUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Get user by ID", description = "Retrieves user profile by their unique identifier. Only returns active (non-deleted) users.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getUserById(
      @Parameter(description = "User UUID", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @Operation(summary = "Get user by phone number", description = "Retrieves user profile by phone number. Useful for SOS service lookups.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User found", content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @GetMapping("/phone/{phone}")
  public ResponseEntity<UserResponse> getUserByPhone(
      @Parameter(description = "Phone number with country code", example = "+8801712345678") @PathVariable String phone) {
    return ResponseEntity.ok(userService.getUserByPhone(phone));
  }

  @Operation(summary = "Update user profile", description = "Updates an existing user's profile. Supports partial updates — only non-null fields will be changed. "
      + "Publishes PROFILE_UPDATED event to Kafka.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request data"),
      @ApiResponse(responseCode = "404", description = "User not found"),
      @ApiResponse(responseCode = "409", description = "Email or phone already in use by another user")
  })
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> updateUser(
      @Parameter(description = "User UUID") @PathVariable UUID id,
      @Valid @RequestBody UserUpdateRequest request) {
    return ResponseEntity.ok(userService.updateUser(id, request));
  }

  @Operation(summary = "Soft-delete user", description = "Deactivates a user account via soft delete. Sets is_active=false and deleted_at timestamp. "
      + "Publishes USER_DELETED event to Kafka.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User deleted successfully"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(
      @Parameter(description = "User UUID") @PathVariable UUID id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  // ===================== User Preferences Endpoints =====================

  @Operation(summary = "Get user preferences", description = "Retrieves notification and safety feature preferences for a user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Preferences found", content = @Content(schema = @Schema(implementation = UserPreferenceResponse.class))),
      @ApiResponse(responseCode = "404", description = "User or preferences not found")
  })
  @GetMapping("/{id}/preferences")
  public ResponseEntity<UserPreferenceResponse> getUserPreferences(
      @Parameter(description = "User UUID") @PathVariable UUID id) {
    return ResponseEntity.ok(userService.getUserPreferences(id));
  }

  @Operation(summary = "Update user preferences", description = "Updates notification and safety feature preferences. Supports partial updates. "
      + "Publishes PROFILE_UPDATED event to Kafka.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Preferences updated successfully", content = @Content(schema = @Schema(implementation = UserPreferenceResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request data"),
      @ApiResponse(responseCode = "404", description = "User or preferences not found")
  })
  @PutMapping("/{id}/preferences")
  public ResponseEntity<UserPreferenceResponse> updateUserPreferences(
      @Parameter(description = "User UUID") @PathVariable UUID id,
      @Valid @RequestBody UserPreferenceRequest request) {
    return ResponseEntity.ok(userService.updateUserPreferences(id, request));
  }
}
