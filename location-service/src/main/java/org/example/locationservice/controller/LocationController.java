package org.example.locationservice.controller;

import org.example.locationservice.model.LocationPing;
import org.example.locationservice.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    /**
     * Updates location and returns a success message
     */
    @PostMapping("/update")
    public ResponseEntity<String> updateLocation(@RequestBody Map<String,Object> body){
        UUID sosId = UUID.fromString((String) body.get("sosId"));
        UUID userId = UUID.fromString((String) body.get("userId"));
        double lat = (Double) body.get("lat");
        double lng = (Double) body.get("lng");

        locationService.updateLocation(sosId,userId,lat,lng);
        return ResponseEntity.ok().body("Location updated successfully");
    }
}
