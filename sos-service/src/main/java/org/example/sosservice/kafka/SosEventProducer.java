package org.example.sosservice.kafka;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.example.sosservice.model.SosCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SosEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${sos.kafka.topic}")
    private String sosTopic;

    /**
     * Publishes SOS triggered event to Kafka topic
     */
    public void publishSosTriggered(SosCase sos, double lat, double lng) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventId", UUID.randomUUID().toString());
        event.put("eventType", "SOS_TRIGGERED");
        event.put("sosId", sos.getId().toString());
        event.put("userId", sos.getUserId().toString());
        event.put("lat", lat);
        event.put("lng", lng);
        event.put("timestamp", Instant.now().toString());

        kafkaTemplate.send(sosTopic, sos.getUserId().toString(), event);
        log.info("Published SOS_TRIGGERED event for userId={}, sosId={}", sos.getUserId(), sos.getId());
    }

    /**
     * Publishes SOS cancelled event to Kafka topic
     */
    public void publishSosCancelled(SosCase sos) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventId", UUID.randomUUID().toString());
        event.put("eventType", "SOS_CANCELLED");
        event.put("sosId", sos.getId().toString());
        event.put("userId", sos.getUserId().toString());
        event.put("cancelReason", sos.getCancelReason());
        event.put("timestamp", Instant.now().toString());

        kafkaTemplate.send(sosTopic, sos.getUserId().toString(), event);
        log.info("Published SOS_CANCELLED event for userId={}, sosId={}", sos.getUserId(), sos.getId());
    }

    /**
     * Publishes SOS resolved event to Kafka topic
     */
    public void publishSosResolved(SosCase sos) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventId", UUID.randomUUID().toString());
        event.put("eventType", "SOS_RESOLVED");
        event.put("sosId", sos.getId().toString());
        event.put("userId", sos.getUserId().toString());
        event.put("timestamp", Instant.now().toString());

        kafkaTemplate.send(sosTopic, sos.getUserId().toString(), event);
        log.info("Published SOS_RESOLVED event for userId={}, sosId={}", sos.getUserId(), sos.getId());
    }
}
