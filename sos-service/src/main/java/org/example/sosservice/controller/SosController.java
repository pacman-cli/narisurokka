package org.example.sosservice.controller;

import java.util.Map;
import java.util.UUID;

import org.example.sosservice.model.SosCase;
import org.example.sosservice.service.SosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/sos")
@Tag(name = "SOS Controller", description = "Emergency SOS alert management endpoints") // ->swagger
public class SosController {
    private final SosService sosService;

    public SosController(SosService sosService) {
        this.sosService = sosService;
    }

    @Operation(summary = "Trigger SOS Alert", description = "Triggers a new emergency SOS alert for a user with their current location. "
            +
            "Publishes event to Kafka and caches in Redis.") // ->swagger
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SOS triggered successfully", content = @Content(schema = @Schema(implementation = SosCase.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "409", description = "Active SOS already exists for this user")
    }) // ->swagger
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "SOS trigger request with user ID and location", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Trigger SOS Example", value = """
            {
                "userId": "123e4567-e89b-12d3-a456-426614174000",
                "lat": 23.8103,
                "lng": 90.4125
            }
            """))) // ->swagger
    @PostMapping("/trigger")
    public ResponseEntity<SosCase> triggerSos(@RequestBody Map<String, Object> request) {
        UUID userId = UUID.fromString((String) request.get("userId"));
        double lat = (Double) request.get("lat");
        double lng = (Double) request.get("lng");

        return ResponseEntity.ok(sosService.triggerSos(userId, lat, lng));
    }

    @Operation(summary = "Cancel SOS Alert", description = "Cancels an active SOS alert for a user with a cancellation reason. "
            +
            "Removes from Redis cache and publishes cancellation event to Kafka.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SOS cancelled successfully", content = @Content(schema = @Schema(implementation = SosCase.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "404", description = "No active SOS found for this user")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "SOS cancel request with user ID and reason", required = true, content = @Content(mediaType = "application/json", examples = @ExampleObject(name = "Cancel SOS Example", value = """
            {
                "userID": "123e4567-e89b-12d3-a456-426614174000",
                "reason": "False alarm - situation resolved"
            }
            """)))
    @PostMapping("/cancel")
    public ResponseEntity<SosCase> cancelSos(@RequestBody Map<String, Object> body) {
        UUID userId = UUID.fromString((String) body.get("userID"));
        String reason = (String) body.get("reason");

        return ResponseEntity.ok(sosService.cancelSos(userId, reason));
    }
}
