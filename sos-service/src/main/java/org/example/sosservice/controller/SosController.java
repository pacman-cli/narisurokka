package org.example.sosservice.controller;

import java.util.UUID;

import org.example.sosservice.dto.SosCancelRequest;
import org.example.sosservice.dto.SosResolveRequest;
import org.example.sosservice.dto.SosTriggerRequest;
import org.example.sosservice.model.SosCase;
import org.example.sosservice.service.SosService;
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
@RequestMapping("/api/v1/sos")
@RequiredArgsConstructor
@Tag(name = "SOS Controller", description = "Emergency SOS alert management endpoints")
public class SosController {

    private final SosService sosService;

    @Operation(summary = "Trigger SOS Alert", description = "Triggers a new emergency SOS alert for a user with their current location. "
            + "Publishes SOS_TRIGGERED event to Kafka and caches active state in Redis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SOS triggered successfully", content = @Content(schema = @Schema(implementation = SosCase.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Active SOS already exists for this user")
    })
    @PostMapping("/trigger")
    public ResponseEntity<SosCase> triggerSos(@Valid @RequestBody SosTriggerRequest request) {
        SosCase sos = sosService.triggerSos(request.getUserId(), request.getLat(), request.getLng());
        return ResponseEntity.ok(sos);
    }

    @Operation(summary = "Cancel SOS Alert", description = "Cancels an active SOS alert for a user with a cancellation reason. "
            + "Removes from Redis cache and publishes SOS_CANCELLED event to Kafka.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SOS cancelled successfully", content = @Content(schema = @Schema(implementation = SosCase.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "No active SOS found for this user")
    })
    @PostMapping("/cancel")
    public ResponseEntity<SosCase> cancelSos(@Valid @RequestBody SosCancelRequest request) {
        SosCase sos = sosService.cancelSos(request.getUserId(), request.getReason());
        return ResponseEntity.ok(sos);
    }

    @Operation(summary = "Resolve SOS Alert", description = "Resolves an active SOS alert (e.g., by responders or authorities). "
            + "Removes from Redis cache and publishes SOS_RESOLVED event to Kafka.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SOS resolved successfully", content = @Content(schema = @Schema(implementation = SosCase.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "No active SOS found for this user")
    })
    @PostMapping("/resolve")
    public ResponseEntity<SosCase> resolveSos(@Valid @RequestBody SosResolveRequest request) {
        SosCase sos = sosService.resolveSos(request.getUserId(), request.getResolutionNotes());
        return ResponseEntity.ok(sos);
    }

    @Operation(summary = "Get Active SOS", description = "Checks if a user has an active SOS case. Returns the active SOS or 404.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active SOS found", content = @Content(schema = @Schema(implementation = SosCase.class))),
            @ApiResponse(responseCode = "404", description = "No active SOS for this user")
    })
    @GetMapping("/active/{userId}")
    public ResponseEntity<SosCase> getActiveSos(
            @Parameter(description = "User UUID", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID userId) {
        return sosService.getActiveSos(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
