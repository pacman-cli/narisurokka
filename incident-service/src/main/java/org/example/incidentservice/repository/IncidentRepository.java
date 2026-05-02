package org.example.incidentservice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.example.incidentservice.model.Incident;
import org.example.incidentservice.model.IncidentStatus;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, UUID> {

  List<Incident> findByReporterIdAndDeletedAtIsNull(UUID reporterId);

  List<Incident> findByStatusInAndDeletedAtIsNull(List<IncidentStatus> statuses);

  @Query("SELECT i FROM Incident i WHERE i.deletedAt IS NULL ORDER BY i.createdAt DESC")
  List<Incident> findAllActive();

  @Query("SELECT i FROM Incident i WHERE i.status = :status AND i.deletedAt IS NULL ORDER BY i.createdAt DESC")
  List<Incident> findByStatus(IncidentStatus status);

  long countByStatusAndDeletedAtIsNull(IncidentStatus status);
}