package org.example.notificationservice.service;

import java.time.Instant;

import org.example.notificationservice.model.Notification;
import org.example.notificationservice.model.NotificationStatus;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Push notification service.
 * Currently uses log-based simulation. For production, integrate Firebase Cloud
 * Messaging (FCM).
 */
@Service
@Slf4j
public class PushNotificationService {

  private final NotificationRepository repository;

  public PushNotificationService(NotificationRepository repository) {
    this.repository = repository;
  }

  public void sendPushNotification(Notification notification) {
    try {
      // TODO: Replace with actual FCM/APNs for production
      log.info("[PUSH] Sending to device: {} | Title: {} | Body: {}",
          notification.getRecipientAddress(),
          notification.getSubject(),
          notification.getMessage());

      // Simulate successful send
      notification.setStatus(NotificationStatus.SENT);
      notification.setSentAt(Instant.now());
      repository.save(notification);

      log.info("[PUSH] Successfully sent notification {} to device {}",
          notification.getId(), notification.getRecipientAddress());
    } catch (Exception e) {
      log.error("[PUSH] Failed to send notification {} to device {}: {}",
          notification.getId(), notification.getRecipientAddress(), e.getMessage());
      notification.setStatus(NotificationStatus.FAILED);
      notification.setErrorMessage(e.getMessage());
      repository.save(notification);
    }
  }
}
