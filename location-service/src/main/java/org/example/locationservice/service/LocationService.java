package org.example.locationservice.service;

import java.util.UUID;

public interface LocationService {
    void updateLocation(UUID sosId,UUID userId,double lat,double lng);
}
