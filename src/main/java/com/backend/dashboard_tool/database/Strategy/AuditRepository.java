package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.Audit;
/**
 * Repository interface for accessing Audit data.
 * This interface extends JpaRepository to provide CRUD operations for the Audit entity.
 */
public interface AuditRepository extends JpaRepository<Audit, Long> {
    //Query to find all Audit entities associated with a specific process ID
    @Query("SELECT a FROM Audit a JOIN a.processes p WHERE p.id = :processId")
    List<Audit> findByProcessId(@Param("processId") Long processId);
}
