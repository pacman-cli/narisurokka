package org.example.incidentservice.controller;

import java.util.List;
import java.util.UUID;

import org.example.incidentservice.dto.IncidentCreateRequest;
import org.example.incidentservice.dto.IncidentResponse;
import org.example.incidentservice.service.IncidentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/incidents")
@RequiredArgsConstructor
@Tag(name = "Incident Controller", description = "Incident reporting and management endpoints")
public class IncidentController {

  private final IncidentService incidentService;

  @Operation(summary = "Create incident report", description = "Submit a new incident report. Can be anonymous.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Incident created", content = @Content(schema = @Schema(implementation = IncidentResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid request data")
  })
  @PostMapping
  public ResponseEntity<IncidentResponse> createIncident(@Valid @RequestBody IncidentCreateRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(incidentService.createIncident(request));
  }

  @Operation(summary = "Get incident by ID", description = "Retrieves a specific incident by ID.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Incident found"),
      @ApiResponse(responseCode = "404", description = "Incident not found")
  })
  @GetMapping("/{id}")
  public ResponseEntity<IncidentResponse> getIncident(
      @Parameter(description = "Incident UUID") @PathVariable UUID id) {
    return ResponseEntity.ok(incidentService.getIncident(id));
  }

  @Operation(summary = "Get all incidents", description = "Retrieves all active incidents.")
  @GetMapping
  public ResponseEntity<List<IncidentResponse>> getAllIncidents() {
    return ResponseEntity.ok(incidentService.getAllIncidents());
  }

  @Operation(summary = "Get incidents by status", description = "Retrieves incidents filtered by status.")
  @GetMapping("/status/{status}")
  public ResponseEntity<List<IncidentResponse>> getIncidentsByStatus(
      @Parameter(description = "Status: SUBMITTED, UNDER_REVIEW, INVESTIGATING, RESOLVED, DISMISSED, ESCALATED")
      @PathVariable String status) {
    return ResponseEntity.ok(incidentService.getIncidentsByStatus(status));
  }

  @Operation(summary = "Get reporter's incidents", description = "Retrieves incidents submitted by a specific user.")
  @GetMapping("/reporter/{reporterId}")
  public ResponseEntity<List<IncidentResponse>> getReporterIncidents(
      @Parameter(description = "Reporter UUID") @PathVariable String reporterId) {
    return ResponseEntity.ok(incidentService.getReporterIncidents(reporterId));
  }

  @Operation(summary = "Update incident status", description = "Updates incident status (authorities only).")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Status updated"),
      @ApiResponse(responseCode = "404", description = "Incident not found")
  })
  @PostMapping("/{id}/status")
  public ResponseEntity<IncidentResponse> updateStatus(
      @Parameter(description = "Incident UUID") @PathVariable UUID id,
      @Parameter(description = "New status") @RequestParam String status,
      @Parameter(description = "Resolution notes") @RequestParam(required = false) String notes) {
    return ResponseEntity.ok(incidentService.updateIncidentStatus(id, status, notes));
  }
}