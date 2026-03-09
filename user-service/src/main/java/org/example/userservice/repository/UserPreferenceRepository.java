package org.example.userservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.userservice.model.UserPreference;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, UUID> {

  Optional<UserPreference> findByUserId(UUID userId);
}
