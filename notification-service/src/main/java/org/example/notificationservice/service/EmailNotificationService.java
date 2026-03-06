package org.example.notificationservice.service;

import java.time.Instant;

import org.example.notificationservice.model.Notification;
import org.example.notificationservice.model.NotificationStatus;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * Email notification service.
 * Currently uses log-based simulation. For production, integrate
 * JavaMailSender.
 */
@Service
@Slf4j
public class EmailNotificationService {

  private final NotificationRepository repository;

  public EmailNotificationService(NotificationRepository repository) {
    this.repository = repository;
  }

  public void sendEmail(Notification notification) {
    try {
      // TODO: Replace with actual JavaMailSender for production
      log.info("[EMAIL] Sending to: {} | Subject: {} | Body: {}",
          notification.getRecipientAddress(),
          notification.getSubject(),
          notification.getMessage());

      // Simulate successful send
      notification.setStatus(NotificationStatus.SENT);
      notification.setSentAt(Instant.now());
      repository.save(notification);

      log.info("[EMAIL] Successfully sent notification {} to {}",
          notification.getId(), notification.getRecipientAddress());
    } catch (Exception e) {
      log.error("[EMAIL] Failed to send notification {} to {}: {}",
          notification.getId(), notification.getRecipientAddress(), e.getMessage());
      notification.setStatus(NotificationStatus.FAILED);
      notification.setErrorMessage(e.getMessage());
      repository.save(notification);
    }
  }
}
