package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.KPI;
/**
 * Repository interface for accessing KPI data.
 * This interface extends JpaRepository to provide CRUD operations for the KPI entity.
 */
public interface KPIRepository extends JpaRepository<KPI, Long> {
    // Query to find all KPI entities associated with a specific process ID
    @Query("SELECT k FROM KPI k JOIN k.processes p WHERE p.id = :processId")
    List<KPI> findByProcessId(@Param("processId") Long processId);
}
