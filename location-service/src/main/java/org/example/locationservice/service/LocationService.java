package org.example.locationservice.service;

import java.util.List;
import java.util.UUID;

import org.example.locationservice.dto.LocationResponse;

public interface LocationService {

    LocationResponse updateLocation(UUID sosId, UUID userId, double lat, double lng);

    LocationResponse getLatestLocation(UUID userId);

    List<LocationResponse> getLocationHistory(UUID sosId);
}
