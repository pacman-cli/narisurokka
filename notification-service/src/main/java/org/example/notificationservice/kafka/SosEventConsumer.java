package org.example.notificationservice.kafka;

import java.util.Map;
import java.util.UUID;

import org.example.notificationservice.model.Notification;
import org.example.notificationservice.model.NotificationType;
import org.example.notificationservice.repository.NotificationRepository;
import org.example.notificationservice.service.EmailNotificationService;
import org.example.notificationservice.service.PushNotificationService;
import org.example.notificationservice.service.SmsNotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SosEventConsumer {

    private final EmailNotificationService emailService;
    private final SmsNotificationService smsService;
    private final PushNotificationService pushService;
    private final NotificationRepository repository;

    @KafkaListener(topics = "${notification.kafka.sos-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeSosEvent(Map<String, Object> event) {
        String eventType = (String) event.get("eventType");
        String userId = (String) event.get("userId");
        String sosId = (String) event.get("sosId");

        log.info("Received SOS event: type={}, userId={}, sosId={}", eventType, userId, sosId);

        if (eventType == null) {
            log.warn("Received SOS event with null eventType, ignoring");
            return;
        }

        switch (eventType) {
            case "SOS_TRIGGERED" -> handleSosTriggered(event, userId, sosId);
            case "SOS_CANCELLED" -> handleSosCancelled(event, userId, sosId);
            case "SOS_RESOLVED" -> handleSosResolved(event, userId, sosId);
            default -> log.warn("Unknown SOS event type: {}", eventType);
        }
    }

    private void handleSosTriggered(Map<String, Object> event, String userId, String sosId) {
        log.info("Processing SOS_TRIGGERED for userId={}", userId);

        // Create and send PUSH notification
        Notification pushNotification = Notification.builder()
                .userId(UUID.fromString(userId))
                .sosId(UUID.fromString(sosId))
                .type(NotificationType.PUSH)
                .subject("🚨 SOS ALERT")
                .message("Emergency SOS has been triggered! User needs immediate help.")
                .recipientAddress("device-token-placeholder")
                .build();
        repository.save(pushNotification);
        pushService.sendPushNotification(pushNotification);

        // Create and send SMS notification
        Notification smsNotification = Notification.builder()
                .userId(UUID.fromString(userId))
                .sosId(UUID.fromString(sosId))
                .type(NotificationType.SMS)
                .subject("SOS ALERT")
                .message("EMERGENCY: Your contact has triggered an SOS alert and needs help immediately!")
                .recipientAddress("phone-placeholder")
                .build();
        repository.save(smsNotification);
        smsService.sendSms(smsNotification);

        // Create and send EMAIL notification
        Notification emailNotification = Notification.builder()
                .userId(UUID.fromString(userId))
                .sosId(UUID.fromString(sosId))
                .type(NotificationType.EMAIL)
                .subject("🚨 NariSurokkha SOS Alert - Immediate Action Required")
                .message("An emergency SOS has been triggered by your contact. Please check on them immediately. "
                        + "SOS ID: " + sosId)
                .recipientAddress("email-placeholder")
                .build();
        repository.save(emailNotification);
        emailService.sendEmail(emailNotification);
    }

    private void handleSosCancelled(Map<String, Object> event, String userId, String sosId) {
        log.info("Processing SOS_CANCELLED for userId={}", userId);

        String reason = (String) event.getOrDefault("cancelReason", "No reason provided");

        Notification notification = Notification.builder()
                .userId(UUID.fromString(userId))
                .sosId(UUID.fromString(sosId))
                .type(NotificationType.PUSH)
                .subject("SOS Alert Cancelled")
                .message("The SOS alert has been cancelled. Reason: " + reason)
                .recipientAddress("device-token-placeholder")
                .build();
        repository.save(notification);
        pushService.sendPushNotification(notification);
    }

    private void handleSosResolved(Map<String, Object> event, String userId, String sosId) {
        log.info("Processing SOS_RESOLVED for userId={}", userId);

        Notification notification = Notification.builder()
                .userId(UUID.fromString(userId))
                .sosId(UUID.fromString(sosId))
                .type(NotificationType.PUSH)
                .subject("SOS Alert Resolved ✅")
                .message("The SOS alert has been resolved. The user is safe.")
                .recipientAddress("device-token-placeholder")
                .build();
        repository.save(notification);
        pushService.sendPushNotification(notification);
    }
}
