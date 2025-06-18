package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.Goal;
/**
 * Repository interface for accessing Goal data.
 * This interface extends JpaRepository to provide CRUD operations for the Goal entity.
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {
    //Query to find all Goal entities associated with a specific process ID
    @Query("SELECT g FROM Goal g JOIN g.processes p WHERE p.id = :processId")
    List<Goal> findByProcessId(@Param("processId") Long processId);
}
