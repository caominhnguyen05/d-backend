package com.backend.dashboard_tool.database.Finance;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.dashboard_tool.entity.Finance.Cost;

/**
 * Repository interface for accessing Cost data.
 * This interface extends JpaRepository to provide CRUD operations for the Cost entity.
 */
public interface CostRepository extends JpaRepository<Cost, Long> {
    /**
     * Retrieves all Cost entities associated with a specific process ID.
     *
     * This JPQL query joins the Cost entity with its related Process entities
     * and filters the results by the given process ID.
     *
     * @param processId The ID of the associated process.
     * @return A list of Cost entities linked to the specified process.
     */
    @Query("SELECT c FROM Cost c JOIN c.processes p WHERE p.id = :processId")
    List<Cost> findByProcessId(@Param("processId") Long processId);
}