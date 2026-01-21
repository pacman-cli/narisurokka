package org.example.sosservice.kafka;

import lombok.RequiredArgsConstructor;
import org.example.sosservice.model.SosCase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SosEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "sos.event";


    /**
     * Publishes SOS triggered event to Kafka topic
     */
    public void publishSosTriggered(SosCase sos, double lat, double lng) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventId", UUID.randomUUID());
        event.put("type", "SOS_TRIGGERED");
        event.put("sosId", sos.getId());
        event.put("userId", sos.getUserId());
        event.put("timestamp", Instant.now().toString());
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("lat", lat);
        metadata.put("lng", lng);
        event.put("metadata", metadata);
        kafkaTemplate.send(TOPIC, sos.getUserId().toString(), event);
    }

    public void publishSosCancelled(SosCase sos) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventId", java.util.UUID.randomUUID());
        event.put("type", "SOS_CANCELLED");
        event.put("sosId", sos.getId());
        event.put("userId", sos.getUserId());
        event.put("timestamp", java.time.Instant.now().toString());

        kafkaTemplate.send(TOPIC, sos.getUserId().toString(), event);
    }
}
