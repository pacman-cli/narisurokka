package org.example.locationservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.locationservice.kafka.LocationEventProducer;
import org.example.locationservice.model.LocationPing;
import org.example.locationservice.service.LocationService;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    //caching
    //geokey prefix
    private final RedisTemplate<String,Object> redisTemplate;
    private final static String GEO_KEY_PREFIX="sos:geo:";
    private final LocationEventProducer locationEventProducer;

    @Override
    public void updateLocation(UUID sosId, UUID userId, double lat, double lng) {
        String key= GEO_KEY_PREFIX + sosId;

        //update location
        GeoOperations<String, java.lang.Object> geoOperations =redisTemplate.opsForGeo();
        //add key with the latitude and longitude
        geoOperations.add(key, new Point(lat,lng),userId.toString());

        // Builds location ping event with the current timestamp
        LocationPing locationPing= LocationPing.builder()
                .sosId(sosId)
                .userId(userId)
                .lat(lat)
                .lng(lng)
                .timestamp(Instant.now())
                .build();
        locationEventProducer.publishLocation(locationPing);
    }
}
