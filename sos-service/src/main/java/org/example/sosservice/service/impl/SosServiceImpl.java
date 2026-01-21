package org.example.sosservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.sosservice.kafka.SosEventProducer;
import org.example.sosservice.model.SosCase;
import org.example.sosservice.model.SosStatus;
import org.example.sosservice.repository.SosRepository;
import org.example.sosservice.service.SosService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SosServiceImpl implements SosService {
    private final SosRepository repository;
    private final SosEventProducer producer;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String ACTIVE_SOS_KEY = "active:sos:user:";

    public SosCase triggerSos(UUID userId, double lat, double lng) {
        // check if an active SOS exists
        repository.findByUserIdAndStatus(userId, SosStatus.ACTIVE).ifPresent(sos -> {
            throw new RuntimeException("Active SOS already exists!");
        });

        SosCase sos = SosCase.builder()
                .userId(userId)
                .status(SosStatus.ACTIVE)
                .triggeredAt(Instant.now())
                .build();

        repository.save(sos);

        // cache in Redis
        String redisKey = ACTIVE_SOS_KEY + userId;
        redisTemplate.opsForValue().set(redisKey, sos.getId().toString(), 1, TimeUnit.HOURS);

        // publish Kafka event
        producer.publishSosTriggered(sos, lat, lng);

        return sos;
    }

    /**
     * Cancels active SOS; publishes cancellation event
     */
    public SosCase cancelSos(UUID userId, String reason) {
        SosCase sos = repository.findByUserIdAndStatus(userId, SosStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("No active SOS found"));

        sos.setStatus(SosStatus.CANCELLED);
        sos.setCancelReason(reason);
        sos.setResolvedAt(Instant.now());
        repository.save(sos);

        redisTemplate.delete(ACTIVE_SOS_KEY + userId);

        producer.publishSosCancelled(sos);

        return sos;
    }
}
