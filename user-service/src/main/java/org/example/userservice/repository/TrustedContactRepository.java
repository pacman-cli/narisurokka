package org.example.userservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.example.userservice.model.TrustedContact;
import org.example.userservice.model.User;

@Repository
public interface TrustedContactRepository extends JpaRepository<TrustedContact, UUID> {

  List<TrustedContact> findByUserAndDeletedAtIsNull(User user);

  List<TrustedContact> findByUserIdAndDeletedAtIsNull(UUID userId);

  Optional<TrustedContact> findByIdAndDeletedAtIsNull(UUID id);

  Optional<TrustedContact> findByUserIdAndIsPrimaryTrueAndDeletedAtIsNull(UUID userId);

  boolean existsByUserAndContactPhoneAndDeletedAtIsNull(User user, String contactPhone);

  @Modifying
  @Query("UPDATE TrustedContact tc SET tc.isPrimary = false WHERE tc.user.id = :userId AND tc.deletedAt IS NULL")
  void clearPrimaryContacts(UUID userId);

  boolean existsByUserAndContactPhoneAndDeletedAtIsNull(User user, String contactPhone);
}