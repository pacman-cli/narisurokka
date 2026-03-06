package org.example.locationservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.locationservice.model.LocationPing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationPingRepository extends JpaRepository<LocationPing, UUID> {

  Optional<LocationPing> findTopByUserIdOrderByTimestampDesc(UUID userId);

  List<LocationPing> findBySosIdOrderByTimestampDesc(UUID sosId);

  List<LocationPing> findByUserIdOrderByTimestampDesc(UUID userId);
}
