package org.example.authservice.kafka;

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
public class AuthEventProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${auth.kafka.topic}")
  private String authTopic;

  public void publishUserRegistered(UUID userId, String email, String role) {
    Map<String, Object> event = new HashMap<>();
    event.put("eventId", UUID.randomUUID().toString());
    event.put("eventType", "USER_REGISTERED");
    event.put("userId", userId.toString());
    event.put("email", email);
    event.put("role", role);
    event.put("timestamp", Instant.now().toString());

    kafkaTemplate.send(authTopic, userId.toString(), event);
    log.info("Published USER_REGISTERED event for userId={}", userId);
  }

  public void publishUserLoggedIn(UUID userId, String email) {
    Map<String, Object> event = new HashMap<>();
    event.put("eventId", UUID.randomUUID().toString());
    event.put("eventType", "USER_LOGGED_IN");
    event.put("userId", userId.toString());
    event.put("email", email);
    event.put("timestamp", Instant.now().toString());

    kafkaTemplate.send(authTopic, userId.toString(), event);
    log.info("Published USER_LOGGED_IN event for userId={}", userId);
  }

  public void publishUserLoggedOut(UUID userId) {
    Map<String, Object> event = new HashMap<>();
    event.put("eventId", UUID.randomUUID().toString());
    event.put("eventType", "USER_LOGGED_OUT");
    event.put("userId", userId.toString());
    event.put("timestamp", Instant.now().toString());

    kafkaTemplate.send(authTopic, userId.toString(), event);
    log.info("Published USER_LOGGED_OUT event for userId={}", userId);
  }
}
