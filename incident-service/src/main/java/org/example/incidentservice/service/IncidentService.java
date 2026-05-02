package org.example.incidentservice.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.incidentservice.dto.IncidentCreateRequest;
import org.example.incidentservice.dto.IncidentResponse;
import org.example.incidentservice.exception.IncidentNotFoundException;
import org.example.incidentservice.kafka.IncidentEventProducer;
import org.example.incidentservice.model.Incident;
import org.example.incidentservice.model.IncidentStatus;
import org.example.incidentservice.model.IncidentType;
import org.example.incidentservice.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncidentService {

  private final IncidentRepository incidentRepository;
  private final IncidentEventProducer eventProducer;

  @Transactional
  public IncidentResponse createIncident(IncidentCreateRequest request) {
    UUID reporterId = request.getReporterId() != null ? UUID.fromString(request.getReporterId()) : null;

    Incident incident = Incident.builder()
        .reporterId(reporterId)
        .anonymousReport(Boolean.TRUE.equals(request.getAnonymousReport()) ||
            (reporterId == null))
        .incidentType(parseIncidentType(request.getIncidentType()))
        .description(request.getDescription())
        .locationAddress(request.getLocationAddress())
        .incidentLat(request.getLat())
        .incidentLng(request.getLng())
        .incidentDate(request.getIncidentDate())
        .witnessCount(request.getWitnessCount())
        .isAnonymousToAuthorities(Boolean.TRUE.equals(request.getIsAnonymousToAuthorities()))
        .status(IncidentStatus.SUBMITTED)
        .build();

    if (request.getEvidenceUrls() != null && !request.getEvidenceUrls().isEmpty()) {
      incident.setEvidenceUrls(String.join(",", request.getEvidenceUrls()));
    }

    incident = incidentRepository.save(incident);

    eventProducer.publishIncidentCreated(incident);

    log.info("Incident created: id={}, type={}, reporterId={}",
        incident.getId(), incident.getIncidentType(), reporterId);

    return mapToResponse(incident);
  }

  @Transactional(readOnly = true)
  public IncidentResponse getIncident(UUID id) {
    Incident incident = incidentRepository.findById(id)
        .orElseThrow(() -> new IncidentNotFoundException("Incident not found: " + id));
    return mapToResponse(incident);
  }

  @Transactional(readOnly = true)
  public List<IncidentResponse> getAllIncidents() {
    return incidentRepository.findAllActive().stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<IncidentResponse> getIncidentsByStatus(String status) {
    IncidentStatus incidentStatus = parseStatus(status);
    return incidentRepository.findByStatus(incidentStatus).stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<IncidentResponse> getReporterIncidents(String reporterId) {
    UUID uuid = UUID.fromString(reporterId);
    return incidentRepository.findByReporterIdAndDeletedAtIsNull(uuid).stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public IncidentResponse updateIncidentStatus(UUID id, String status, String notes) {
    Incident incident = incidentRepository.findById(id)
        .orElseThrow(() -> new IncidentNotFoundException("Incident not found: " + id));

    incident.setStatus(parseStatus(status));
    incident.setUpdatedAt(Instant.now());

    if (notes != null) {
      incident.setResolvedNotes(notes);
    }

    if (status.equals("RESOLVED") || status.equals("DISMISSED")) {
      incident.setResolvedAt(Instant.now());
    }

    incident = incidentRepository.save(incident);

    eventProducer.publishIncidentUpdated(incident);

    log.info("Incident status updated: id={}, status={}", id, status);
    return mapToResponse(incident);
  }

  private IncidentType parseIncidentType(String type) {
    try {
      return IncidentType.valueOf(type.toUpperCase());
    } catch (IllegalArgumentException e) {
      return IncidentType.OTHER;
    }
  }

  private IncidentStatus parseStatus(String status) {
    try {
      return IncidentStatus.valueOf(status.toUpperCase());
    } catch (IllegalArgumentException e) {
      return IncidentStatus.SUBMITTED;
    }
  }

  private IncidentResponse mapToResponse(Incident incident) {
    List<String> evidenceUrls = incident.getEvidenceUrls() != null
        ? List.of(incident.getEvidenceUrls().split(","))
        : List.of();

    return IncidentResponse.builder()
        .id(incident.getId().toString())
        .reporterId(incident.getReporterId() != null && !incident.getAnonymousReport()
            ? incident.getReporterId().toString() : null)
        .anonymousReport(incident.getAnonymousReport())
        .incidentType(incident.getIncidentType().name())
        .description(incident.getDescription())
        .locationAddress(incident.getLocationAddress())
        .lat(incident.getIncidentLat())
        .lng(incident.getIncidentLng())
        .status(incident.getStatus().name())
        .incidentDate(incident.getIncidentDate())
        .witnessCount(incident.getWitnessCount())
        .evidenceUrls(evidenceUrls)
        .isAnonymousToAuthorities(incident.getIsAnonymousToAuthorities())
        .resolvedNotes(incident.getResolvedNotes())
        .createdAt(incident.getCreatedAt())
        .updatedAt(incident.getUpdatedAt())
        .resolvedAt(incident.getResolvedAt())
        .build();
  }
}