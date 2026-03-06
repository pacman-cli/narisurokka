package org.example.notificationservice.repository;

import java.util.List;
import java.util.UUID;

import org.example.notificationservice.model.Notification;
import org.example.notificationservice.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

  List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);

  List<Notification> findBySosIdOrderByCreatedAtDesc(UUID sosId);

  List<Notification> findByStatus(NotificationStatus status);
}
