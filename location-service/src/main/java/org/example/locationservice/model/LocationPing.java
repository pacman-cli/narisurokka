package org.example.locationservice.model;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationPing {
    private UUID sosId;
    private UUID userId;
    private double lat;
    private double lng;
    private Instant timestamp;
}
