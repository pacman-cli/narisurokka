package org.example.sosservice.service.impl;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.example.sosservice.exception.ActiveSosExistsException;
import org.example.sosservice.exception.SosNotFoundException;
import org.example.sosservice.kafka.SosEventProducer;
import org.example.sosservice.model.SosCase;
import org.example.sosservice.model.SosStatus;
import org.example.sosservice.repository.SosRepository;
import org.example.sosservice.service.SosService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SosServiceImpl implements SosService {

    private final SosRepository repository;
    private final SosEventProducer producer;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ACTIVE_SOS_KEY = "active:sos:user:";

    @Override
    @Transactional
    public SosCase triggerSos(UUID userId, double lat, double lng) {
        // Check if an active SOS already exists
        repository.findByUserIdAndStatus(userId, SosStatus.ACTIVE).ifPresent(sos -> {
            throw new ActiveSosExistsException("Active SOS already exists for user " + userId);
        });

        SosCase sos = SosCase.builder()
                .userId(userId)
                .status(SosStatus.ACTIVE)
                .triggeredAt(Instant.now())
                .build();

        repository.save(sos);

        // Cache in Redis
        String redisKey = ACTIVE_SOS_KEY + userId;
        redisTemplate.opsForValue()
                .set(
                        redisKey, sos.getId().toString(), 1, TimeUnit.HOURS);

        // Publish Kafka event
        producer.publishSosTriggered(sos, lat, lng);

        log.info("SOS triggered: userId={}, sosId={}", userId, sos.getId());
        return sos;
    }

    @Override
    @Transactional
    public SosCase cancelSos(UUID userId, String reason) {
        SosCase sos = repository.findByUserIdAndStatus(userId, SosStatus.ACTIVE)
                .orElseThrow(() -> new SosNotFoundException("No active SOS found for user " + userId));

        sos.setStatus(SosStatus.CANCELLED);
        sos.setCancelReason(reason);
        sos.setResolvedAt(Instant.now());
        repository.save(sos);

        // Remove from Redis cache
        redisTemplate.delete(ACTIVE_SOS_KEY + userId);

        // Publish Kafka event
        producer.publishSosCancelled(sos);

        log.info("SOS cancelled: userId={}, sosId={}, reason={}", userId, sos.getId(), reason);
        return sos;
    }

    @Override
    @Transactional
    public SosCase resolveSos(UUID userId, String resolutionNotes) {
        SosCase sos = repository.findByUserIdAndStatus(userId, SosStatus.ACTIVE)
                .orElseThrow(() -> new SosNotFoundException("No active SOS found for user " + userId));

        sos.setStatus(SosStatus.RESOLVED);
        sos.setResolvedAt(Instant.now());
        if (resolutionNotes != null && !resolutionNotes.isBlank()) {
            sos.setCancelReason(resolutionNotes); // reusing field for notes
        }
        repository.save(sos);

        // Remove from Redis cache
        redisTemplate.delete(ACTIVE_SOS_KEY + userId);

        // Publish Kafka event
        producer.publishSosResolved(sos);

        log.info("SOS resolved: userId={}, sosId={}", userId, sos.getId());
        return sos;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SosCase> getActiveSos(UUID userId) {
        return repository.findByUserIdAndStatus(userId, SosStatus.ACTIVE);
    }
}
