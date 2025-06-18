package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.Risk;
/**
 * Repository interface for accessing Risk data.
 * This interface extends JpaRepository to provide CRUD operations for the Risk entity.
 */
public interface RiskRepository extends JpaRepository<Risk, Long> {
    //Query to find all Risk entities associated with a specific process ID
    @Query("SELECT r FROM Risk r JOIN r.processes p WHERE p.id = :processId")
    List<Risk> findByProcessId(@Param("processId") Long processId);
}
