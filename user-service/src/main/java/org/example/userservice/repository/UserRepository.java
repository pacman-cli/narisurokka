package org.example.userservice.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.userservice.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmailAndDeletedAtIsNull(String email);

  Optional<User> findByPhoneAndDeletedAtIsNull(String phone);

  Optional<User> findByIdAndDeletedAtIsNull(UUID id);

  boolean existsByEmailAndDeletedAtIsNull(String email);

  boolean existsByPhoneAndDeletedAtIsNull(String phone);
}
