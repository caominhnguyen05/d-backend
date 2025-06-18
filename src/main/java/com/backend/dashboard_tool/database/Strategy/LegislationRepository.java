package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.Legislation;
/**
 * Repository interface for accessing Legislation data.
 * This interface extends JpaRepository to provide CRUD operations for the Legislation entity.
 */
public interface LegislationRepository extends JpaRepository<Legislation, Long> {
    //Query to find all Legislation entities associated with a specific process ID
    @Query("SELECT l FROM Legislation l JOIN l.processes p WHERE p.id = :processId")
    List<Legislation> findByProcessId(@Param("processId") Long processId);
}
