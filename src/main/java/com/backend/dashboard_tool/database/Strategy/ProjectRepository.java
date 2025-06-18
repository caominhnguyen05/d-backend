package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.Project;
/**
 * Repository interface for accessing Project data.
 * This interface extends JpaRepository to provide CRUD operations for the Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project, Long> {
    //Query to find all Project entities associated with a specific process ID
    @Query("SELECT pr FROM Project pr JOIN pr.processes p WHERE p.id = :processId")
    List<Project> findByProcessId(@Param("processId") Long processId);
}
