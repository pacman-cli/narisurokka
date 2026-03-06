package org.example.notificationservice.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.notificationservice.dto.NotificationResponse;
import org.example.notificationservice.model.Notification;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Controller", description = "Notification history and management endpoints")
public class NotificationController {

  private final NotificationRepository repository;

  @Operation(summary = "Get user notifications", description = "Retrieves all notifications for a user, ordered by most recent first.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Notifications returned")
  })
  @GetMapping("/user/{userId}")
  public ResponseEntity<List<NotificationResponse>> getUserNotifications(
      @Parameter(description = "User UUID") @PathVariable UUID userId) {
    List<NotificationResponse> notifications = repository.findByUserIdOrderByCreatedAtDesc(userId)
        .stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
    return ResponseEntity.ok(notifications);
  }

  @Operation(summary = "Get SOS notifications", description = "Retrieves all notifications associated with a specific SOS case.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Notifications returned")
  })
  @GetMapping("/sos/{sosId}")
  public ResponseEntity<List<NotificationResponse>> getSosNotifications(
      @Parameter(description = "SOS Case UUID") @PathVariable UUID sosId) {
    List<NotificationResponse> notifications = repository.findBySosIdOrderByCreatedAtDesc(sosId)
        .stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
    return ResponseEntity.ok(notifications);
  }

  @Operation(summary = "Health check", description = "Simple health check endpoint")
  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("Notification Service is running");
  }

  private NotificationResponse mapToResponse(Notification notification) {
    return NotificationResponse.builder()
        .id(notification.getId())
        .userId(notification.getUserId())
        .sosId(notification.getSosId())
        .type(notification.getType().name())
        .status(notification.getStatus().name())
        .subject(notification.getSubject())
        .message(notification.getMessage())
        .recipientAddress(notification.getRecipientAddress())
        .createdAt(notification.getCreatedAt())
        .sentAt(notification.getSentAt())
        .build();
  }
}
