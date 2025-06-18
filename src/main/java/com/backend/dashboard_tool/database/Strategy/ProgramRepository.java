package com.backend.dashboard_tool.database.Strategy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Strategy.Program;
/**
 * Repository interface for accessing Program data.
 * This interface extends JpaRepository to provide CRUD operations for the Program entity.
 */
public interface ProgramRepository extends JpaRepository<Program, Long> {
    //Query to find all Program entities associated with a specific process ID
    @Query("SELECT pr FROM Program pr JOIN pr.processes p WHERE p.id = :processId")
    List<Program> findByProcessId(@Param("processId") Long processId);
}
