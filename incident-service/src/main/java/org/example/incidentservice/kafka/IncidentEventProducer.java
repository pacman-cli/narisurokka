package org.example.incidentservice.kafka;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.example.incidentservice.model.Incident;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IncidentEventProducer {

  private final KafkaTemplate<String, Object> kafkaTemplate;

  @Value("${incident.kafka.topic:incident.events}")
  private String topic;

  public void publishIncidentCreated(Incident incident) {
    Map<String, Object> event = Map.of(
        "eventType", "INCIDENT_CREATED",
        "incidentId", incident.getId().toString(),
        "reporterId", incident.getReporterId() != null ? incident.getReporterId().toString() : "anonymous",
        "incidentType", incident.getIncidentType().name(),
        "status", incident.getStatus().name(),
        "anonymousReport", incident.getAnonymousReport(),
        "timestamp", Instant.now().toString()
    );

    kafkaTemplate.send(topic, incident.getId().toString(), event);
    log.info("Published INCIDENT_CREATED event: incidentId={}", incident.getId());
  }

  public void publishIncidentUpdated(Incident incident) {
    Map<String, Object> event = Map.of(
        "eventType", "INCIDENT_UPDATED",
        "incidentId", incident.getId().toString(),
        "previousStatus", "SUBMITTED",
        "newStatus", incident.getStatus().name(),
        "timestamp", Instant.now().toString()
    );

    kafkaTemplate.send(topic, incident.getId().toString(), event);
    log.info("Published INCIDENT_UPDATED event: incidentId={}, status={}",
        incident.getId(), incident.getStatus());
  }
}