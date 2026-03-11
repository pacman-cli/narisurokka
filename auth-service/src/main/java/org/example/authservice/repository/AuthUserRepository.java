package org.example.authservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.example.authservice.model.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {

  Optional<AuthUser> findByEmail(String email);

  Optional<AuthUser> findByPhone(String phone);

  boolean existsByEmail(String email);

  boolean existsByPhone(String phone);
}
