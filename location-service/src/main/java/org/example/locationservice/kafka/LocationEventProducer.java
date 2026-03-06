package org.example.locationservice.kafka;

import java.util.HashMap;
import java.util.Map;

import org.example.locationservice.model.LocationPing;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${location.kafka.topic}")
    private String locationTopic;

    public void publishLocation(LocationPing ping) {
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "LOCATION_UPDATED");
        event.put("sosId", ping.getSosId().toString());
        event.put("userId", ping.getUserId().toString());
        event.put("lat", ping.getLat());
        event.put("lng", ping.getLng());
        event.put("timestamp", ping.getTimestamp().toString());

        kafkaTemplate.send(locationTopic, ping.getSosId().toString(), event);
        log.debug("Published LOCATION_UPDATED event for sosId={}, userId={}", ping.getSosId(), ping.getUserId());
    }
}
