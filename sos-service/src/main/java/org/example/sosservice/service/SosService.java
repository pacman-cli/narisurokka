package org.example.sosservice.service;

import java.util.Optional;
import java.util.UUID;

import org.example.sosservice.model.SosCase;

public interface SosService {
    SosCase triggerSos(UUID userId, double lat, double lng);

    SosCase cancelSos(UUID userId, String reason);

    SosCase resolveSos(UUID userId, String resolutionNotes);

    Optional<SosCase> getActiveSos(UUID userId);
}
