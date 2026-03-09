package org.example.userservice.kafka;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${user.kafka.topic}")
  private String userTopic;

  public void publishUserRegistered(UUID userId, String email, String phone) {
    Map<String, Object> event = new HashMap<>();
    event.put("eventType", "USER_REGISTERED");
    event.put("userId", userId.toString());
    event.put("email", email);
    event.put("phone", phone);
    event.put("timestamp", Instant.now().toString());

    kafkaTemplate.send(userTopic, userId.toString(), event);
    log.info("Published USER_REGISTERED event for userId={}", userId);
  }

  public void publishProfileUpdated(UUID userId) {
    Map<String, Object> event = new HashMap<>();
    event.put("eventType", "PROFILE_UPDATED");
    event.put("userId", userId.toString());
    event.put("timestamp", Instant.now().toString());

    kafkaTemplate.send(userTopic, userId.toString(), event);
    log.info("Published PROFILE_UPDATED event for userId={}", userId);
  }

  public void publishUserDeleted(UUID userId) {
    Map<String, Object> event = new HashMap<>();
    event.put("eventType", "USER_DELETED");
    event.put("userId", userId.toString());
    event.put("timestamp", Instant.now().toString());

    kafkaTemplate.send(userTopic, userId.toString(), event);
    log.info("Published USER_DELETED event for userId={}", userId);
  }
}
