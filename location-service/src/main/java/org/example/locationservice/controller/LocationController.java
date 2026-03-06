package org.example.locationservice.controller;

import java.util.List;
import java.util.UUID;

import org.example.locationservice.dto.LocationResponse;
import org.example.locationservice.dto.LocationUpdateRequest;
import org.example.locationservice.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
@Tag(name = "Location Controller", description = "Real-time GPS location tracking endpoints")
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "Update user location", description = "Updates the live GPS location for a user during an active SOS. "
            + "Stores in Redis GEO, persists to DB, publishes Kafka event, and broadcasts via WebSocket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location updated successfully", content = @Content(schema = @Schema(implementation = LocationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping("/update")
    public ResponseEntity<LocationResponse> updateLocation(@Valid @RequestBody LocationUpdateRequest request) {
        LocationResponse response = locationService.updateLocation(
                request.getSosId(), request.getUserId(), request.getLat(), request.getLng());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get latest location", description = "Retrieves the most recent location ping for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Latest location found", content = @Content(schema = @Schema(implementation = LocationResponse.class))),
            @ApiResponse(responseCode = "404", description = "No location found for this user")
    })
    @GetMapping("/latest/{userId}")
    public ResponseEntity<LocationResponse> getLatestLocation(
            @Parameter(description = "User UUID") @PathVariable UUID userId) {
        return ResponseEntity.ok(locationService.getLatestLocation(userId));
    }

    @Operation(summary = "Get location history", description = "Retrieves all location pings for a specific SOS case, ordered by most recent first.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Location history returned")
    })
    @GetMapping("/history/{sosId}")
    public ResponseEntity<List<LocationResponse>> getLocationHistory(
            @Parameter(description = "SOS Case UUID") @PathVariable UUID sosId) {
        return ResponseEntity.ok(locationService.getLocationHistory(sosId));
    }
}
