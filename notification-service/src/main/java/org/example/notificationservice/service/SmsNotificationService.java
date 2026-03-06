package org.example.notificationservice.service;

import java.time.Instant;

import org.example.notificationservice.model.Notification;
import org.example.notificationservice.model.NotificationStatus;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * SMS notification service.
 * Currently uses log-based simulation. For production, integrate Twilio or
 * similar SMS gateway.
 */
@Service
@Slf4j
public class SmsNotificationService {

  private final NotificationRepository repository;

  public SmsNotificationService(NotificationRepository repository) {
    this.repository = repository;
  }

  public void sendSms(Notification notification) {
    try {
      // TODO: Replace with actual SMS gateway (Twilio, AWS SNS) for production
      log.info("[SMS] Sending to: {} | Message: {}",
          notification.getRecipientAddress(),
          notification.getMessage());

      // Simulate successful send
      notification.setStatus(NotificationStatus.SENT);
      notification.setSentAt(Instant.now());
      repository.save(notification);

      log.info("[SMS] Successfully sent notification {} to {}",
          notification.getId(), notification.getRecipientAddress());
    } catch (Exception e) {
      log.error("[SMS] Failed to send notification {} to {}: {}",
          notification.getId(), notification.getRecipientAddress(), e.getMessage());
      notification.setStatus(NotificationStatus.FAILED);
      notification.setErrorMessage(e.getMessage());
      repository.save(notification);
    }
  }
}
