package com.backend.dashboard_tool.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Process_Data.Trigger;

/**
 * Repository interface for accessing Trigger entities in the database.
 * This interface extends JpaRepository to provide CRUD operations and custom queries.
 */
public interface TriggerRepository extends JpaRepository<Trigger, Long> {
    /**
     * Finds all Trigger entities associated with a specific target process ID.
     *
     * @param id the ID of the target process
     * @return a list of Trigger entities associated with the specified target process ID
     */
    @Query("SELECT t FROM Trigger t WHERE t.targetProcess.id = :id")
    List<Trigger> findByTargetProcessId(@Param("id") Long id);
}
