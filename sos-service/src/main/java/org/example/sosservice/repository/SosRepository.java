package org.example.sosservice.repository;

import org.example.sosservice.model.SosCase;
import org.example.sosservice.model.SosStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SosRepository extends JpaRepository<SosCase,UUID>{
    Optional<SosCase> findByUserIdAndStatus(UUID userid, SosStatus status);
}
