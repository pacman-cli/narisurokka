package org.example.sosservice.service;

import org.example.sosservice.model.SosCase;

import java.util.UUID;

public interface SosService {
    SosCase triggerSos(UUID userId, double lat, double lng);

    SosCase cancelSos(UUID userId, String reason);
}
