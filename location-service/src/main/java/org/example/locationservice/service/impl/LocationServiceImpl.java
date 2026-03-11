package org.example.locationservice.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.example.locationservice.dto.LocationResponse;
import org.example.locationservice.exception.LocationNotFoundException;
import org.example.locationservice.kafka.LocationEventProducer;
import org.example.locationservice.model.LocationPing;
import org.example.locationservice.repository.LocationPingRepository;
import org.example.locationservice.service.LocationService;
import org.example.locationservice.websocket.LocationWebsocketHandler;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final LocationPingRepository repository;
    private final LocationEventProducer locationEventProducer;
    private final LocationWebsocketHandler websocketHandler;

    private static final String GEO_KEY_PREFIX = "sos:geo:";

    @Override
    @Transactional
    public LocationResponse updateLocation(UUID sosId, UUID userId, double lat, double lng) {
        String key = GEO_KEY_PREFIX + sosId;

        // Update Redis GEO (Note: Redis GEO uses longitude-first, latitude-second)
        GeoOperations<String, Object> geoOperations = redisTemplate.opsForGeo();
        geoOperations.add(key, new Point(lng, lat), userId.toString());

        // Persist to database
        LocationPing locationPing = LocationPing.builder()
                .sosId(sosId)
                .userId(userId)
                .lat(lat)
                .lng(lng)
                .timestamp(Instant.now())
                .build();
        repository.save(locationPing);

        // Publish Kafka event
        locationEventProducer.publishLocation(locationPing);

        // Broadcast via WebSocket
        websocketHandler.broadcast(locationPing);

        log.debug("Location updated: sosId={}, userId={}, lat={}, lng={}", sosId, userId, lat, lng);
        return mapToResponse(locationPing);
    }

    @Override
    @Transactional(readOnly = true)
    public LocationResponse getLatestLocation(UUID userId) {
        LocationPing ping = repository.findTopByUserIdOrderByTimestampDesc(userId)
                .orElseThrow(() -> new LocationNotFoundException("No location found for user " + userId));
        return mapToResponse(ping);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LocationResponse> getLocationHistory(UUID sosId) {
        return repository.findBySosIdOrderByTimestampDesc(sosId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private LocationResponse mapToResponse(LocationPing ping) {
        return LocationResponse.builder()
                .id(ping.getId())
                .sosId(ping.getSosId())
                .userId(ping.getUserId())
                .lat(ping.getLat())
                .lng(ping.getLng())
                .timestamp(ping.getTimestamp())
                .build();
    }
}
